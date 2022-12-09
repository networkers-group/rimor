package st.networkers.rimor.command;

import java.util.Collection;

/**
 * Contains a command instruction and subcommands mappings.
 *
 * @see AbstractRimorCommand
 */
public interface RimorCommand {

    Collection<RimorCommand> getSubcommands();

}
