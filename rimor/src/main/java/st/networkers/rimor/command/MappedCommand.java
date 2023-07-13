package st.networkers.rimor.command;

import st.networkers.rimor.instruction.Instruction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Resolved command to use internally. To create commands, check {@link CommandDefinition}.
 *
 * @see CommandDefinition
 * @see AbstractCommandDefinition
 */
public class MappedCommand {

    public static Builder builder() {
        return new Builder();
    }

    private final List<String> identifiers;
    private final Instruction mainInstruction;
    private final Map<String, Instruction> instructions;
    private final Map<String, MappedCommand> subcommands;

    public MappedCommand(List<String> identifiers,
                         Instruction mainInstruction,
                         Map<String, Instruction> instructions,
                         Map<String, MappedCommand> subcommands) {
        this.identifiers = identifiers.stream().map(String::toLowerCase).collect(Collectors.toList());
        this.mainInstruction = mainInstruction;
        this.instructions = instructions;
        this.subcommands = subcommands;
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
        return Objects.equals(identifiers, that.identifiers) && Objects.equals(mainInstruction, that.mainInstruction) && Objects.equals(instructions, that.instructions) && Objects.equals(subcommands, that.subcommands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiers, mainInstruction, instructions, subcommands);
    }

    public static class Builder {
        private List<String> identifiers;
        private Instruction mainInstruction;
        private Map<String, Instruction> instructions;
        private Map<String, MappedCommand> subcommands;

        public Builder identifiers(List<String> identifiers) {
            this.identifiers = identifiers;
            return this;
        }

        public Builder mainInstruction(Instruction mainInstruction) {
            this.mainInstruction = mainInstruction;
            return this;
        }

        public Builder instructions(Map<String, Instruction> instructions) {
            this.instructions = instructions;
            return this;
        }

        public Builder subcommands(Collection<MappedCommand> subcommands) {
            Map<String, MappedCommand> mappedSubcommands = new HashMap<>();
            subcommands.forEach(subcommand -> subcommand.getIdentifiers().forEach(identifier -> mappedSubcommands.put(identifier.toLowerCase(), subcommand)));
            return this.subcommands(mappedSubcommands);
        }

        private Builder subcommands(Map<String, MappedCommand> subcommands) {
            this.subcommands = subcommands;
            return this;
        }

        public MappedCommand create() {
            return new MappedCommand(identifiers, mainInstruction, instructions, subcommands);
        }
    }
}
