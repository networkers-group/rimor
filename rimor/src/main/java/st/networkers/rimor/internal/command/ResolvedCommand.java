package st.networkers.rimor.internal.command;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.instruction.CommandInstruction;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ResolvedCommand {

    @Nullable private final ResolvedCommand parent;
    private final Command commandInstance;
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

    public Collection<CommandInstruction> getInstructions(String alias) {
        return this.instructions.get(alias.toLowerCase());
    }

    public ResolvedCommand getSubcommand(String alias) {
        return this.subcommands.get(alias.toLowerCase());
    }

    public Set<String> getAllInstructionAliases() {
        return this.instructions.keySet();
    }
}