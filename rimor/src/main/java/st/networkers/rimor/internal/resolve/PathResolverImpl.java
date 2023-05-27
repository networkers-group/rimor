package st.networkers.rimor.internal.resolve;

import st.networkers.rimor.Executable;
import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.resolve.InstructionNotFoundException;
import st.networkers.rimor.resolve.PathResolver;
import st.networkers.rimor.resolve.ResolvedPath;

import java.util.Collections;
import java.util.List;

public class PathResolverImpl implements PathResolver {

    private final ExceptionHandlerRegistry exceptionHandlerRegistry;
    private final ExecutionTaskRegistry executionTaskRegistry;

    public PathResolverImpl(ExceptionHandlerRegistry exceptionHandlerRegistry, ExecutionTaskRegistry executionTaskRegistry) {
        this.exceptionHandlerRegistry = exceptionHandlerRegistry;
        this.executionTaskRegistry = executionTaskRegistry;
    }

    @Override
    public ResolvedPath resolvePath(MappedCommand command, List<String> path, ExecutionContext context) {
        this.runPreExecutionTasks(command, context);
        return this.resolveInstruction(command, command, path, context);
    }

    private ResolvedPath resolveInstruction(MappedCommand uberCommand, MappedCommand command, List<String> path, ExecutionContext context) {
        if (path.isEmpty())
            return this.resolveMainInstruction(uberCommand, command, path, context);

        return command.getInstruction(path.get(0))
                .map(instruction -> this.runPreExecutionTasks(instruction, context))
                .map(instruction -> new ResolvedPath(uberCommand, instruction, Collections.unmodifiableList(this.nextPath(path))))
                .orElseGet(() -> this.resolveSubcommand(uberCommand, command, path, context));
    }

    private ResolvedPath resolveMainInstruction(MappedCommand uberCommand, MappedCommand command, List<String> path, ExecutionContext context) {
        return command.getMainInstruction()
                .map(instruction -> new ResolvedPath(uberCommand, instruction, Collections.unmodifiableList(path)))
                .orElseThrow(() -> new InstructionNotFoundException(uberCommand, command, Collections.unmodifiableList(path)));
    }

    private ResolvedPath resolveSubcommand(MappedCommand uberCommand, MappedCommand command, List<String> path, ExecutionContext context) {
        return command.getSubcommand(path.get(0))
                .map(subcommand -> this.runPreExecutionTasks(subcommand, context))
                .map(subcommand -> this.resolveInstruction(uberCommand, subcommand, this.nextPath(path), context))
                .orElseGet(() -> this.resolveMainInstruction(uberCommand, command, path, context));
    }

    private <T extends Executable> T runPreExecutionTasks(T executable, ExecutionContext context) {
        try {
            executionTaskRegistry.getPreExecutionTasks().forEach(task -> {
                if (executable.matchesAnnotations(task))
                    task.run(executable, context);
            });
        } catch (Throwable throwable) {
            exceptionHandlerRegistry.handleException(throwable, context);
        }

        return executable;
    }

    private List<String> nextPath(List<String> path) {
        return path.size() > 1 ? path.subList(1, path.size()) : Collections.emptyList();
    }
}
