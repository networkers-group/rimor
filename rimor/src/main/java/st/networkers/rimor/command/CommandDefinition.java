package st.networkers.rimor.command;

import java.util.Collection;

/**
 * Represents a command definition: its instructions and subcommands.
 *
 * @see AbstractCommandDefinition
 */
public interface CommandDefinition {

    Collection<CommandDefinition> getSubcommands();

}
