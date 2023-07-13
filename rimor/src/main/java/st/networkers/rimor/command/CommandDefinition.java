package st.networkers.rimor.command;

import java.util.Collection;

/**
 * Contains instruction and subcommand mappings.
 * <p>
 * There is no need to implement this in order to define a command. This is only intended to allow registering
 * subcommand instances. See the {@link Command} documentation for instructions on how to define a command.
 *
 * @see AbstractCommandDefinition
 * @see Command
 */
public interface CommandDefinition {

    Collection<Object> getSubcommands();

}
