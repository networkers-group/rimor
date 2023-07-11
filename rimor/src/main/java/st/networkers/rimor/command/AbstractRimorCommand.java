package st.networkers.rimor.command;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Contains instruction and subcommand mappings.
 * <p>
 * There is no need to extend this in order to define a command. This is only intended to allow registering
 * subcommand instances. See the {@link Command} documentation for instructions on how to define a command.
 *
 * @see Command
 */
public abstract class AbstractRimorCommand implements RimorCommand {

    private final Collection<Object> subcommands = new ArrayList<>();

    protected final void registerSubcommand(@NotNull Object subcommand) {
        this.subcommands.add(subcommand);
    }

    @Override
    public Collection<Object> getSubcommands() {
        return subcommands;
    }
}
