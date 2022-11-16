package st.networkers.rimor.internal.command;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.Executable;
import st.networkers.rimor.command.AbstractCommandDefinition;
import st.networkers.rimor.command.CommandDefinition;
import st.networkers.rimor.internal.instruction.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolved command to use internally. To define a command, check {@link CommandDefinition}.
 *
 * @see CommandDefinition
 * @see AbstractCommandDefinition
 */
public class Command implements Executable {

    @Nullable private final Command parent;
    @Getter private final CommandDefinition definition;
    private final Collection<String> aliases;

    @Getter @Setter private Instruction mainInstruction;
    private final Map<String, Instruction> instructions = new HashMap<>();
    private final Map<String, Command> subcommands = new HashMap<>();

    public Command(@Nullable Command parent, CommandDefinition definition, Collection<String> aliases) {
        this.parent = parent;
        this.definition = definition;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public Optional<Command> getParent() {
        return Optional.ofNullable(parent);
    }

    public void registerInstruction(Instruction instruction) {
        for (String alias : instruction.getAliases())
            this.instructions.put(alias, instruction);
    }

    public void registerSubcommand(Command subcommand) {
        for (String alias : subcommand.getAliases())
            this.subcommands.put(alias, subcommand);
    }

    public Collection<String> getAliases() {
        return Collections.unmodifiableCollection(this.aliases);
    }

    public Instruction getInstruction(String alias) {
        return this.instructions.get(alias.toLowerCase());
    }

    public Map<String, Instruction> getInstructions() {
        return Collections.unmodifiableMap(this.instructions);
    }

    public Command getSubcommand(String alias) {
        return this.subcommands.get(alias.toLowerCase());
    }

    public Map<String, Command> getSubcommands() {
        return Collections.unmodifiableMap(this.subcommands);
    }

    public Set<String> getAllInstructionAliases() {
        return this.instructions.keySet();
    }
}
