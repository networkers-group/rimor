package st.networkers.rimor.internal.command;

import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.command.instruction.CommandInstruction;

import java.util.*;

public class RimorCommand {

    @Nullable private final RimorCommand parent;
    private final Command command;

    private final List<String> aliases;
    private final Instructions instructions;
    private final Map<String, RimorCommand> subcommands = new HashMap<>();

    public RimorCommand(@Nullable RimorCommand parent,
                        Command command,
                        List<String> aliases,
                        Instructions instructions) {
        this.parent = parent;
        this.command = command;
        this.aliases = aliases;
        this.instructions = instructions;
    }

    public Optional<RimorCommand> getParent() {
        return Optional.ofNullable(parent);
    }

    public Command getCommand() {
        return this.command;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public List<CommandInstruction> getMainInstructions() {
        return this.instructions.getMainInstructions();
    }

    public List<CommandInstruction> getInstructions(String alias) {
        return this.instructions.getInstructions(alias);
    }

    public void registerSubcommand(RimorCommand subcommand) {
        for (String alias : subcommand.getAliases())
            this.subcommands.put(alias, subcommand);
    }

    public Map<String, RimorCommand> getSubcommands() {
        return this.subcommands;
    }

    public Set<String> getAllAliases() {
        Set<String> aliases = this.subcommands.keySet();
        aliases.addAll(this.instructions.getAllAliases());
        return aliases;
    }


    public static class Instructions {
        private final List<CommandInstruction> mainInstructions = new ArrayList<>();
        private final Map<String, List<CommandInstruction>> instructions = new HashMap<>();

        public void cacheMainInstruction(CommandInstruction instruction) {
            this.mainInstructions.add(instruction);
        }

        public List<CommandInstruction> getMainInstructions() {
            return this.mainInstructions;
        }

        public void cacheInstruction(CommandInstruction instruction) {
            for (String alias : instruction.getAliases())
                this.instructions.computeIfAbsent(alias, a -> new ArrayList<>()).add(instruction);
        }

        public List<CommandInstruction> getInstructions(String alias) {
            return this.instructions.get(alias);
        }

        public Set<String> getAllAliases() {
            return this.instructions.keySet();
        }
    }
}
