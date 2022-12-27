package st.networkers.rimor.internal.execute;

import st.networkers.rimor.Executable;
import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.CommandExecutor;
import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.task.ExecutionTask;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.instruction.Instruction;

import java.util.Collection;

public class CommandExecutorImpl implements CommandExecutor {

    private final RimorInjector injector;
    private final ExceptionHandlerRegistry exceptionHandlerRegistry;
    private final ExecutionTaskRegistry executionTaskRegistry;

    public CommandExecutorImpl(RimorInjector injector, ExceptionHandlerRegistry exceptionHandlerRegistry,
                               ExecutionTaskRegistry executionTaskRegistry) {
        this.injector = injector;
        this.exceptionHandlerRegistry = exceptionHandlerRegistry;
        this.executionTaskRegistry = executionTaskRegistry;
    }

    @Override
    public Object execute(Instruction instruction, ExecutionContext context) {
        return this.execute(instruction, context, false);
    }

    @Override
    public Object execute(Instruction instruction, ExecutionContext context, boolean skipPreExecutionTasks) {
        try {
            if (!skipPreExecutionTasks)
                this.runExecutionTasks(instruction, context, executionTaskRegistry.getPreExecutionTasks());

            Object result = injector.invokeMethod(instruction.getMethod(), instruction.getCommandInstance(), context);

            this.runExecutionTasks(instruction, context, executionTaskRegistry.getPostExecutionTasks());
            return result;
        } catch (Throwable throwable) {
            exceptionHandlerRegistry.handleException(throwable);
            return null;
        }
    }

    private void runExecutionTasks(Executable executable, ExecutionContext context, Collection<? extends ExecutionTask> tasks) {
        if (executable instanceof Instruction) {
            Instruction instruction = (Instruction) executable;
            this.runExecutionTasks(instruction.getCommand(), context, tasks);
        } else if (executable instanceof MappedCommand) {
            MappedCommand command = (MappedCommand) executable;
            command.getParent().ifPresent(parent -> this.runExecutionTasks(parent, context, tasks));
        }

        tasks.forEach(task -> {
            if (executable.matchesAnnotations(task))
                task.run(executable, context);
        });
    }
}
