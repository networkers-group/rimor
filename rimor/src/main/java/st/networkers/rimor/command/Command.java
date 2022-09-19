package st.networkers.rimor.command;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    private final List<Command> subcommands = new ArrayList<>();

    protected final void registerSubcommand(@NotNull Command command) {
        this.subcommands.add(command);
    }

    public List<Command> getSubcommands() {
        return subcommands;
    }
}
