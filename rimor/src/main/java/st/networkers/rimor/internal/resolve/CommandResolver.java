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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandResolver {

    private CommandResolver() {
    }

    public static MappedCommand resolve(RimorCommand command) {
        return resolve(null, command);
    }

    public static MappedCommand resolve(MappedCommand parent, RimorCommand command) {
        List<String> aliases = InspectionUtils.getAliases(command.getClass());

        MappedCommand mappedCommand = new MappedCommand(parent,
                command,
                InspectionUtils.getName(command.getClass()).orElseGet(() -> aliases.get(0)),
                aliases,
                ReflectionUtils.getMappedAnnotations(command.getClass()));

        resolveSubcommands(mappedCommand).forEach(mappedCommand::registerSubcommand);
        resolveInstructions(mappedCommand).apply(mappedCommand);

        return mappedCommand;
    }

    private static List<MappedCommand> resolveSubcommands(MappedCommand mappedCommand) {
        return mappedCommand.getCommand().getSubcommands().stream()
                .map(subcommand -> resolve(mappedCommand, subcommand))
                .collect(Collectors.toList());
    }

    private static class InstructionLookupResults {
        private Instruction mainInstruction;
        private final Collection<Instruction> instructions = new ArrayList<>();

        public void setMainInstruction(Instruction mainInstruction) {
            this.mainInstruction = mainInstruction;
        }

        public void addInstruction(Instruction instruction) {
            this.instructions.add(instruction);
        }

        public void apply(MappedCommand command) {
            command.setMainInstruction(mainInstruction);
            instructions.forEach(command::registerInstruction);
        }
    }

    private static InstructionLookupResults resolveInstructions(MappedCommand mappedCommand) {
        InstructionLookupResults results = new InstructionLookupResults();

        for (Method method : mappedCommand.getCommand().getClass().getMethods()) {
            if (method.isAnnotationPresent(MainInstructionMapping.class))
                results.setMainInstruction(Instruction.build(mappedCommand, method, Collections.emptyList()));

            if (method.isAnnotationPresent(InstructionMapping.class))
                results.addInstruction(Instruction.build(mappedCommand, method, InspectionUtils.getAliases(method)));
        }

        return results;
    }
}
