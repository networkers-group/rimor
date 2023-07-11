package st.networkers.rimor.command;

import st.networkers.rimor.instruction.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolved command to use internally. To create commands, check {@link RimorCommand}.
 *
 * @see RimorCommand
 * @see AbstractRimorCommand
 */
public class MappedCommand {

    private final Object commandInstance;

    private final List<String> identifiers;
    private final Instruction mainInstruction;
    private final Map<String, Instruction> instructions;
    private final Map<String, MappedCommand> subcommands;

    public MappedCommand(Object commandInstance,
                         List<String> identifiers,
                         Instruction mainInstruction,
                         Map<String, Instruction> instructions,
                         Map<String, MappedCommand> subcommands) {
        this.commandInstance = commandInstance;
        this.identifiers = identifiers.stream().map(String::toLowerCase).collect(Collectors.toList());
        this.mainInstruction = mainInstruction;
        this.instructions = instructions;
        this.subcommands = subcommands;
    }

    public Object getCommandInstance() {
        return commandInstance;
    }

    public Collection<String> getIdentifiers() {
        return Collections.unmodifiableCollection(this.identifiers);
    }

    public Optional<Instruction> getMainInstruction() {
        return Optional.ofNullable(this.mainInstruction);
    }

    public Optional<Instruction> getInstruction(String identifier) {
        return Optional.ofNullable(this.instructions.get(identifier.toLowerCase()));
    }

    public Map<String, Instruction> getInstructions() {
        return Collections.unmodifiableMap(this.instructions);
    }

    public Optional<MappedCommand> getSubcommand(String identifier) {
        return Optional.ofNullable(this.subcommands.get(identifier.toLowerCase()));
    }

    public Map<String, MappedCommand> getSubcommands() {
        return Collections.unmodifiableMap(this.subcommands);
    }

    public Set<String> getAllInstructionIdentifiers() {
        return this.instructions.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MappedCommand)) return false;
        MappedCommand that = (MappedCommand) o;
        return Objects.equals(commandInstance, that.commandInstance) && Objects.equals(identifiers, that.identifiers) && Objects.equals(mainInstruction, that.mainInstruction) && Objects.equals(instructions, that.instructions) && Objects.equals(subcommands, that.subcommands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandInstance, identifiers, mainInstruction, instructions, subcommands);
    }
}
