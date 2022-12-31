package st.networkers.rimor.resolve;

import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.context.ExecutionContext;

import java.util.List;

/**
 * Resolver for simple string paths, like:
 * <ul>
 *     <li>["echo", "Hello", "world!"] -> command "echo", main instruction, leftoverPath ["Hello", "world!"]</li>
 *     <li>["git", "checkout", "dev"] -> command "git", instruction "checkout", leftoverPath ["dev"]</li>
 *     <li>["setActive", "true"] -> command "setActive", main instruction, leftoverPath ["true"]</li>
 * </ul>
 */
public interface PathResolver {

    ResolvedPath resolvePath(MappedCommand command, List<String> path, ExecutionContext context);

}
