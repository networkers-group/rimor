package st.networkers.rimor.internal.resolve;

import st.networkers.rimor.Executable;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.task.ExecutionEnclosingTaskRegistry;
import st.networkers.rimor.execute.task.PreExecutionTask;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.command.Command;
import st.networkers.rimor.internal.execute.ExecutionEnclosingTaskException;
import st.networkers.rimor.internal.instruction.Instruction;
import st.networkers.rimor.resolve.InstructionNotFoundException;
import st.networkers.rimor.resolve.PathResolver;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PathResolverImpl implements PathResolver {

    private final ExecutionEnclosingTaskRegistry executionTaskRegistry;
    private final Injector injector;

    public PathResolverImpl(ExecutionEnclosingTaskRegistry executionTaskRegistry, Injector injector) {
        this.executionTaskRegistry = executionTaskRegistry;
        this.injector = injector;
    }

    @Override
    public Results resolvePath(Command command, List<String> path, ExecutionContext context) {
        this.runPreExecutionTasks(command, context);
        return this.resolveInstruction(command, command, path, context);
    }

    private Results resolveInstruction(Command uberCommand, Command command, List<String> path, ExecutionContext context) {
        if (path.isEmpty())
            return this.resolveMainInstruction(uberCommand, command, path, context);

        return command.getInstruction(path.get(0))
                .map(instruction -> this.runPreExecutionTasks(instruction, context))
                .map(instruction -> new Results(uberCommand, instruction, Collections.unmodifiableList(this.nextPath(path))))
                .orElseGet(() -> this.resolveSubcommand(uberCommand, command, path, context));
    }

    private Results resolveMainInstruction(Command uberCommand, Command command, List<String> path, ExecutionContext context) {
        return command.getMainInstruction()
                .map(instruction -> new Results(uberCommand, instruction, Collections.unmodifiableList(path)))
                .orElseThrow(() -> new InstructionNotFoundException(uberCommand, command, Collections.unmodifiableList(path)));
    }

    private Results resolveSubcommand(Command uberCommand, Command command, List<String> path, ExecutionContext context) {
        return command.getSubcommand(path.get(0))
                .map(subcommand -> this.runPreExecutionTasks(subcommand, context))
                .map(subcommand -> this.resolveInstruction(uberCommand, subcommand, this.nextPath(path), context))
                .orElseGet(() -> this.resolveMainInstruction(uberCommand, command, path, context));
    }

    private Command runPreExecutionTasks(Command command, ExecutionContext context) {
        this.forEachPreExecutionTask(command, preExecutionTask -> preExecutionTask.run(command, injector, context));
        return command;
    }

    private Instruction runPreExecutionTasks(Instruction instruction, ExecutionContext context) {
        this.forEachPreExecutionTask(instruction, preExecutionTask -> preExecutionTask.run(instruction, injector, context));
        return instruction;
    }

    private void forEachPreExecutionTask(Executable executable, Consumer<PreExecutionTask> runnable) {
        for (PreExecutionTask preExecutionTask : executionTaskRegistry.getPreExecutionTasks())
            if (executable.matchesAnnotations(preExecutionTask)) {
                try {
                    runnable.accept(preExecutionTask);
                } catch (Throwable throwable) {
                    throw new ExecutionEnclosingTaskException(executable, preExecutionTask, throwable);
                }
            }
    }

    private List<String> nextPath(List<String> path) {
        return path.size() > 1 ? path.subList(1, path.size()) : Collections.emptyList();
    }
}
