package st.networkers.rimor.command;

import st.networkers.rimor.instruction.Instruction;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedCommandBuilder {

    private Object commandInstance;
    private List<String> identifiers;
    private Instruction mainInstruction;
    private Map<String, Instruction> instructions;
    private Map<String, MappedCommand> subcommands;

    public MappedCommandBuilder setCommandInstance(Object commandInstance) {
        this.commandInstance = commandInstance;
        return this;
    }

    public MappedCommandBuilder setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
        return this;
    }

    public MappedCommandBuilder setMainInstruction(Instruction mainInstruction) {
        this.mainInstruction = mainInstruction;
        return this;
    }

    public MappedCommandBuilder setInstructions(Collection<Instruction> instructions) {
        Map<String, Instruction> mappedInstructions = new HashMap<>();
        instructions.forEach(instruction -> instruction.getIdentifiers().forEach(identifier -> mappedInstructions.put(identifier.toLowerCase(), instruction)));
        return this.setInstructions(mappedInstructions);
    }

    private MappedCommandBuilder setInstructions(Map<String, Instruction> instructions) {
        this.instructions = instructions;
        return this;
    }

    public MappedCommandBuilder setSubcommands(Collection<MappedCommand> subcommands) {
        Map<String, MappedCommand> mappedSubcommands = new HashMap<>();
        subcommands.forEach(subcommand -> subcommand.getIdentifiers().forEach(identifier -> mappedSubcommands.put(identifier.toLowerCase(), subcommand)));
        return this.setSubcommands(mappedSubcommands);
    }

    private MappedCommandBuilder setSubcommands(Map<String, MappedCommand> subcommands) {
        this.subcommands = subcommands;
        return this;
    }

    public MappedCommand create() {
        return new MappedCommand(commandInstance, identifiers, mainInstruction, instructions, subcommands);
    }
}