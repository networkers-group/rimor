package st.networkers.rimor.interpret;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.internal.command.Command;

import java.util.List;

/**
 * Thrown if the execution path does not belong to any instruction and there are no main instructions for the command.
 * <p>
 * For example, the command
 * <pre>
 *     &#64;Aliases("git")
 *     public class GitCommand extends AbstractCommandDefinition {
 *         &#64;Instruction
 *         public void commit(@Params List\<String> params) {
 *              // whatever
 *         }
 *
 *         &#64;Instruction
 *         public void push(@Params List\<String> params) {
 *              // whatever
 *         }
 *     }
 * </pre>
 * will throw an {@link InstructionNotFoundException} if {@code git pull} is executed.
 * <p>
 * However, if a main instruction is present:
 * <pre>
 *     &#64;Aliases("git")
 *     public class GitCommand extends AbstractCommandDefinition {
 *         &#64;MainInstruction
 *         public void main(@Params List<String> params) {
 *              // whatever
 *         }
 *     }
 * </pre>
 * will not throw anything if {@code git pull} is executed, because the main instruction will be run and (if using the
 * rimor-params extension), {@code "pull"} will be passed as a parameter.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InstructionNotFoundException extends RuntimeException {
    private @Nullable final Command uberCommand;
    private @Nullable final Command subCommand;
    private final List<String> remainingPath;
}
