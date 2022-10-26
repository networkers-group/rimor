package st.networkers.rimor.command;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractCommand implements Command {

    private final Collection<Command> subcommands = new ArrayList<>();

    protected final void registerSubcommand(@NotNull Command command) {
        this.subcommands.add(command);
    }

    @Override
    public Collection<Command> getSubcommands() {
        return subcommands;
    }
}
