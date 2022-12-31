package st.networkers.rimor.internal.resolve;

import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.InstructionMapping;
import st.networkers.rimor.instruction.MainInstructionMapping;
import st.networkers.rimor.util.InspectionUtils;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandResolver {

    private CommandResolver() {
    }

    public static MappedCommand resolve(RimorCommand command) {
        return resolve(null, command);
    }

    public static MappedCommand resolve(MappedCommand parent, RimorCommand command) {
        MappedCommand mappedCommand = new MappedCommand(
                parent,
                command,
                InspectionUtils.getAliases(command.getClass()),
                ReflectionUtils.getMappedAnnotations(command.getClass())
        );

        InstructionLookupResults results = resolveInstructions(mappedCommand);
        mappedCommand.setMainInstruction(results.getMainInstruction());
        results.getInstructions().forEach(mappedCommand::registerInstruction);

        resolveSubcommands(mappedCommand).forEach(mappedCommand::registerSubcommand);

        return mappedCommand;
    }

    private static class InstructionLookupResults {
        private Instruction mainInstruction;
        private final Collection<Instruction> instructions = new ArrayList<>();

        public Instruction getMainInstruction() {
            return mainInstruction;
        }

        public void setMainInstruction(Instruction mainInstruction) {
            this.mainInstruction = mainInstruction;
        }

        public void addInstruction(Instruction instruction) {
            this.instructions.add(instruction);
        }

        public Collection<Instruction> getInstructions() {
            return instructions;
        }
    }

    private static InstructionLookupResults resolveInstructions(MappedCommand mappedCommand) {
        InstructionLookupResults results = new InstructionLookupResults();

        for (Method method : mappedCommand.getCommand().getClass().getMethods()) {
            Instruction instruction = Instruction.build(mappedCommand, method, InspectionUtils.getAliases(method));

            if (method.isAnnotationPresent(MainInstructionMapping.class))
                results.setMainInstruction(instruction);

            if (method.isAnnotationPresent(InstructionMapping.class))
                results.addInstruction(instruction);
        }

        return results;
    }

    private static List<MappedCommand> resolveSubcommands(MappedCommand mappedCommand) {
        return mappedCommand.getCommand().getSubcommands().stream()
                .map(subcommand -> resolve(mappedCommand, subcommand))
                .collect(Collectors.toList());
    }
}
