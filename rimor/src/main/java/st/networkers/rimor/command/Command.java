package st.networkers.rimor.command;

import java.util.Collection;

/**
 * @see AbstractCommand
 */
public interface Command {

    Collection<Command> getSubcommands();

}
