package st.networkers.rimor.command;

import java.util.Collection;

/**
 * @see AbstractCommandDefinition
 */
public interface CommandDefinition {

    Collection<CommandDefinition> getSubcommands();

}
