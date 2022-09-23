package st.networkers.rimor.internal.command;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.instruction.CommandInstruction;

import java.util.*;

@Getter
public class RimorCommand {

    @Nullable private final RimorCommand parent;
    private final Command commandInstance;
    private final List<String> aliases;

    private final List<CommandInstruction> mainInstructions = new ArrayList<>();
    private final Map<String, List<CommandInstruction>> instructions = new HashMap<>();
    private final Map<String, RimorCommand> subcommands = new HashMap<>();

    public RimorCommand(@Nullable RimorCommand parent,
                        Command commandInstance,
                        List<String> aliases) {
        this.parent = parent;
        this.commandInstance = commandInstance;
        this.aliases = aliases;
    }

    public Optional<RimorCommand> getParent() {
        return Optional.ofNullable(parent);
    }

    public void registerMainInstruction(CommandInstruction instruction) {
        this.mainInstructions.add(instruction);
    }

    public void registerInstruction(CommandInstruction instruction) {
        for (String alias : instruction.getAliases())
            this.instructions.computeIfAbsent(alias, a -> new ArrayList<>()).add(instruction);
    }

    public List<CommandInstruction> getInstructions(String alias) {
        return this.instructions.get(alias);
    }

    public Set<String> getAllInstructionAliases() {
        return this.instructions.keySet();
    }

    public void registerSubcommand(RimorCommand subcommand) {
        for (String alias : subcommand.getAliases())
            this.subcommands.put(alias, subcommand);
    }
}
