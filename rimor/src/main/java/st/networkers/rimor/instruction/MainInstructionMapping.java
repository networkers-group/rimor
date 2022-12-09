package st.networkers.rimor.instruction;

import st.networkers.rimor.internal.command.MappedCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The main instruction for a {@link MappedCommand}. If the command is called without any argument (or the first
 * argument doesn't match any instruction or subcommand), the method with this annotation will be executed.
 * <p>
 * For example, having this command:
 * <pre>
 *     &#64;CommandMapping("git")
 *     public class GitCommand extends AbstractRimorCommand {
 *
 *         // rimor-params extension for @Params :)
 *         &#64;MainInstructionMapping
 *         public void main(@Params {@literal List<Object>} params) {
 *              // whatever
 *         }
 *
 *         &#64;InstructionMapping
 *         public void commit() {
 *              // whatever
 *         }
 *
 *         &#64;InstructionMapping
 *         public void push() {
 *              // whatever
 *         }
 *     }
 * </pre>
 * The main instruction will be called if {@code git}, {@code git foo}, or {@code git pull} is executed (note that the
 * {@code pull} instruction is not declared), and {@code "foo"} or {@code "pull"} will be passed as params in {@code #main}
 * if using the rimor-params extension.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MainInstructionMapping {
}
