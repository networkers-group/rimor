package st.networkers.rimor.internal.command;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.instruction.CommandInstruction;

import java.util.*;
import java.util.stream.Collectors;

public class ResolvedCommand {

    @Nullable private final ResolvedCommand parent;
    @Getter private final Command commandInstance;
    private final Collection<String> aliases;

    private final Collection<CommandInstruction> mainInstructions = new ArrayList<>();
    private final Map<String, Collection<CommandInstruction>> instructions = new HashMap<>();
    private final Map<String, ResolvedCommand> subcommands = new HashMap<>();

    public ResolvedCommand(@Nullable ResolvedCommand parent,
                           Command commandInstance,
                           Collection<String> aliases) {
        this.parent = parent;
        this.commandInstance = commandInstance;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public Optional<ResolvedCommand> getParent() {
        return Optional.ofNullable(parent);
    }

    public void registerMainInstruction(CommandInstruction instruction) {
        this.mainInstructions.add(instruction);
    }

    public void registerInstruction(CommandInstruction instruction) {
        for (String alias : instruction.getAliases())
            this.instructions.computeIfAbsent(alias, a -> new ArrayList<>()).add(instruction);
    }

    public void registerSubcommand(ResolvedCommand subcommand) {
        for (String alias : subcommand.getAliases())
            this.subcommands.put(alias, subcommand);
    }

    public Collection<String> getAliases() {
        return Collections.unmodifiableCollection(this.aliases);
    }

    public Collection<CommandInstruction> getMainInstructions() {
        return Collections.unmodifiableCollection(mainInstructions);
    }

    public Collection<CommandInstruction> getInstructions(String alias) {
        return this.instructions.get(alias.toLowerCase());
    }

    public Map<String, Collection<CommandInstruction>> getInstructions() {
        return Collections.unmodifiableMap(this.instructions);
    }

    public ResolvedCommand getSubcommand(String alias) {
        return this.subcommands.get(alias.toLowerCase());
    }

    public Map<String, ResolvedCommand> getSubcommands() {
        return Collections.unmodifiableMap(this.subcommands);
    }

    public Set<String> getAllInstructionAliases() {
        return this.instructions.keySet();
    }
}
