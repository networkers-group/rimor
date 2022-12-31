package st.networkers.rimor.resolve;

import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.command.MappedCommand;

import java.util.List;
import java.util.Objects;

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
public class InstructionNotFoundException extends RuntimeException {

    private @Nullable final MappedCommand uberCommand;
    private @Nullable final MappedCommand subcommand;
    private final List<String> remainingPath;

    public InstructionNotFoundException(@Nullable MappedCommand uberCommand, @Nullable MappedCommand subcommand, List<String> remainingPath) {
        this.uberCommand = uberCommand;
        this.subcommand = subcommand;
        this.remainingPath = remainingPath;
    }

    public @Nullable MappedCommand getUberCommand() {
        return uberCommand;
    }

    public @Nullable MappedCommand getSubcommand() {
        return subcommand;
    }

    public List<String> getRemainingPath() {
        return remainingPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstructionNotFoundException)) return false;
        InstructionNotFoundException that = (InstructionNotFoundException) o;
        return Objects.equals(uberCommand, that.uberCommand) && Objects.equals(subcommand, that.subcommand) && Objects.equals(remainingPath, that.remainingPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uberCommand, subcommand, remainingPath);
    }
}
