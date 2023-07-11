package st.networkers.rimor.resolve;

import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.command.MappedCommand;

import java.util.List;
import java.util.Objects;

/**
 * Thrown if the execution path does not lead to any instruction and there are no main instructions for the command.
 * <p>
 * For example, having this command with mappings for {@code commit} and {@code push} instructions, an
 * {@link InstructionNotFoundException} will be thrown if {@code "git foo"} is executed:
 * <pre>
 * &#64;Command("git")
 * public class GitCommand {
 *
 *     &#64;InstructionMapping
 *     public void commit() {
 *         ...
 *     }
 *
 *     &#64;InstructionMapping
 *     public void push() {
 *         ...
 *     }
 * }
 * </pre>
 * However, if a main instruction mapping is present, nothing will be thrown if {@code "git foo"} is executed, because the main
 * instruction will be run and (if using the rimor-params extension), {@code "foo"} will be passed as a parameter.
 * <pre>
 * &#64;Command("git")
 * public class GitCommand {
 *
 *     &#64;MainInstructionMapping
 *     public void displayGitInstructions(@InstructionParams {@literal List<Object>} params) {
 *         ...
 *     }
 * }
 * </pre>
 */
public class InstructionNotFoundException extends RuntimeException {

    private @Nullable final MappedCommand subcommand;
    private final List<String> remainingPath;

    public InstructionNotFoundException(@Nullable MappedCommand subcommand, List<String> remainingPath) {
        this.subcommand = subcommand;
        this.remainingPath = remainingPath;
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
        return Objects.equals(subcommand, that.subcommand) && Objects.equals(remainingPath, that.remainingPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subcommand, remainingPath);
    }
}
