package st.networkers.rimor.command;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractCommandDefinition implements CommandDefinition {

    private final Collection<CommandDefinition> subcommands = new ArrayList<>();

    protected final void registerSubcommand(@NotNull CommandDefinition subcommand) {
        this.subcommands.add(subcommand);
    }

    @Override
    public Collection<CommandDefinition> getSubcommands() {
        return subcommands;
    }
}
