package st.networkers.rimor.command;

import java.util.Collection;

/**
 * Contains instruction and subcommand mappings.
 *
 * @see AbstractRimorCommand
 */
public interface RimorCommand {

    Collection<RimorCommand> getSubcommands();

}
