package st.networkers.rimor.command;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @see CommandMapping
 * @see st.networkers.rimor.instruction.MainInstructionMapping
 * @see st.networkers.rimor.instruction.InstructionMapping
 */
public abstract class AbstractRimorCommand implements RimorCommand {

    private final Collection<RimorCommand> subcommands = new ArrayList<>();

    protected final void registerSubcommand(@NotNull RimorCommand subcommand) {
        this.subcommands.add(subcommand);
    }

    @Override
    public Collection<RimorCommand> getSubcommands() {
        return subcommands;
    }
}
