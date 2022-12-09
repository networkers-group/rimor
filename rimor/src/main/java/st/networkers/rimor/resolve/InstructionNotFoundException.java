package st.networkers.rimor.resolve;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.internal.command.MappedCommand;

import java.util.List;

/**
 * Thrown if the execution path does not belong to any instruction and there are no main instructions for the command.
 * <p>
 * For example, the command
 * <pre>
 *     &#64;CommandMapping("git")
 *     public class GitCommand extends AbstractRimorCommand {
 *         &#64;InstructionMapping
 *         public void commit(@Params {@literal List<String>} params) {
 *              // whatever
 *         }
 *
 *         &#64;InstructionMapping
 *         public void push(@Params {@literal List<String>} params) {
 *              // whatever
 *         }
 *     }
 * </pre>
 * will throw an {@link InstructionNotFoundException} if {@code git pull} is executed.
 * <p>
 * However, if a main instruction is present:
 * <pre>
 *     &#64;CommandMapping("git")
 *     public class GitCommand extends AbstractRimorCommand {
 *         &#64;MainInstructionMapping
 *         public void main(@Params {@literal List<String>} params) {
 *              // whatever
 *         }
 *     }
 * </pre>
 * will not throw anything if {@code git pull} is executed, because the main instruction will be run and (if using the
 * rimor-params extension), {@code "pull"} will be passed as a parameter.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstructionNotFoundException extends RuntimeException {
    private @Nullable final MappedCommand uberCommand;
    private @Nullable final MappedCommand subcommand;
    private final List<String> remainingPath;
}
