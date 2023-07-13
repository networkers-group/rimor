package st.networkers.rimor.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that a class is a Rimor command definition and contains instruction and subcommand mappings for it.
 * <p>
 * If it is an inner class within another command definition class, it will be treated as a subcommand of that command.
 * The inner class has to be public and have a no-arg constructor. It can be whether static or not.
 * <p>
 * The path {@code "myCommand mySubcommand myInstruction"} leads to {@code MyCommand.MySubcommand#instruction()} in
 * this example:
 * <pre>
 * &#64;Command("myCommand")
 * public class MyCommand {
 *
 *     &#64;Command("mySubcommand")
 *     public class MySubcommand {
 *
 *         &#64;InstructionMapping("myInstruction")
 *         public void instruction() {
 *             ...
 *         }
 *     }
 * }
 * </pre>
 * <p>
 * If you need to manually instantiate {@code MySubcommand}, make the parent command class extend {@link AbstractCommandDefinition}
 * and use {@link AbstractCommandDefinition#registerSubcommand(Object)} in the constructor:
 * <pre>
 * &#64;Command("myCommand")
 * public class MyCommand extends AbstractCommandDefinition {
 *
 *     public MyCommand() {
 *         registerSubcommand(new MySubcommand(new MyServiceImpl()));
 *     }
 *
 *     &#64;Command("mySubcommand")
 *     public static class MySubcommand {
 *
 *         public MySubcommand(MyService myService) {
 *             ...
 *         }
 *
 *         &#64;InstructionMapping("myInstruction")
 *         public void instruction() {
 *             ...
 *         }
 *     }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * The identifiers of the command.
     */
    String[] value();
}
