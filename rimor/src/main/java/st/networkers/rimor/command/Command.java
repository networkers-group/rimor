package st.networkers.rimor.command;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public abstract class Command {

    private final Collection<Command> subcommands = new ArrayList<>();

    protected final void registerSubcommand(@NotNull Command command) {
        this.subcommands.add(command);
    }
}
