package st.networkers.rimor.instruction;

import st.networkers.rimor.command.MappedCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The main instruction for a {@link MappedCommand}. If the command is called without any argument (or the first
 * argument doesn't match any instruction or subcommand), this instruction will be executed.
 * <p>
 * For example, having this command with mappings for {@code commit} and {@code push} instructions:
 * <pre>
 * &#64;Command("git")
 * public class GitCommand {
 *
 *     &#64;MainInstructionMapping
 *     public void displayGitInstructions(@InstructionParams List&lt;Object> params) {
 *         ...
 *     }
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
 * The main instruction will be called if {@code "git"}, {@code "git foo"}, or {@code "git bar"} is executed (as
 * {@code "foo"} and {@code "bar"} don't have any mappings for this command), and they will be passed as params
 * if using the rimor-params extension.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MainInstructionMapping {
}
