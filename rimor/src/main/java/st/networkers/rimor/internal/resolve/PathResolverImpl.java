package st.networkers.rimor.internal.resolve;

import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.resolve.InstructionNotFoundException;
import st.networkers.rimor.resolve.PathResolver;
import st.networkers.rimor.resolve.ResolvedPath;

import java.util.Collections;
import java.util.List;

public class PathResolverImpl implements PathResolver {

    @Override
    public ResolvedPath resolvePath(MappedCommand command, List<String> path, ExecutionContext context) {
        return this.resolveInstruction(command, path, context);
    }

    private ResolvedPath resolveInstruction(MappedCommand command, List<String> path, ExecutionContext context) {
        if (path.isEmpty())
            return this.resolveMainInstruction(command, path, context);

        return command.getInstruction(path.get(0))
                .map(instruction -> new ResolvedPath(instruction, Collections.unmodifiableList(this.nextPath(path))))
                .orElseGet(() -> this.resolveSubcommand(command, path, context));
    }

    private ResolvedPath resolveMainInstruction(MappedCommand command, List<String> path, ExecutionContext context) {
        return command.getMainInstruction()
                .map(instruction -> new ResolvedPath(instruction, Collections.unmodifiableList(path)))
                .orElseThrow(() -> new InstructionNotFoundException(command, Collections.unmodifiableList(path)));
    }

    private ResolvedPath resolveSubcommand(MappedCommand command, List<String> path, ExecutionContext context) {
        return command.getSubcommand(path.get(0))
                .map(subcommand -> this.resolveInstruction(subcommand, this.nextPath(path), context))
                .orElseGet(() -> this.resolveMainInstruction(command, path, context));
    }

    private List<String> nextPath(List<String> path) {
        return path.size() > 1 ? path.subList(1, path.size()) : Collections.emptyList();
    }
}
