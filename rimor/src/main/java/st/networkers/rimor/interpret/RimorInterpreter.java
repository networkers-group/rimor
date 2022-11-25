package st.networkers.rimor.interpret;

import lombok.Data;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.command.Command;
import st.networkers.rimor.internal.instruction.Instruction;

import java.util.List;

/**
 * Interpreter for simple string commands, like:
 * <ul>
 *     <li>["echo", "Hello", "world!"] -> command "echo", main instruction, leftoverPath ["Hello", "world!"]</li>
 *     <li>["git", "checkout", "dev"] -> command "git", instruction "checkout", leftoverPath ["dev"]</li>
 *     <li>["setActive", "true"] -> command "setActive", main instruction, leftoverPath ["true"]</li>
 * </ul>
 */
public interface RimorInterpreter {

    @Data
    class Results {
        private final Command mainCommand;
        private final Instruction instruction;
        private final List<String> leftoverPath;
    }

    Results resolveInstruction(Command command, List<String> path, ExecutionContext context);

}
