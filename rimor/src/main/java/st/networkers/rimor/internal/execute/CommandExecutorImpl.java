package st.networkers.rimor.internal.execute;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.CommandExecutor;
import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.instruction.Instruction;

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
        try {
            this.runPreExecutionTasks(instruction, context);
            Object result = injector.invokeMethod(instruction.getMethod(), instruction.getCommandInstance(), context);
            this.runPostExecutionTasks(instruction, context);

            return result;
        } catch (Throwable throwable) {
            this.handleThrowable(throwable, instruction, context);
            return null;
        }
    }

    private void runPreExecutionTasks(Instruction instruction, ExecutionContext context) {
        instruction.getExecutionTaskRegistry().runPreExecutionTasks(instruction, context);
        executionTaskRegistry.runPreExecutionTasks(instruction, context);
    }

    private void runPostExecutionTasks(Instruction instruction, ExecutionContext context) {
        instruction.getExecutionTaskRegistry().runPostExecutionTasks(instruction, context);
        executionTaskRegistry.runPostExecutionTasks(instruction, context);
    }

    private void handleThrowable(Throwable throwable, Instruction instruction, ExecutionContext context) {
        try {
            instruction.getExceptionHandlerRegistry().handleException(throwable, context);
        } catch (Throwable rethrown) {
            exceptionHandlerRegistry.handleException(rethrown, context);
        }
    }
}
