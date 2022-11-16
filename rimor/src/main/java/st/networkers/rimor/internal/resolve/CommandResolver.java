package st.networkers.rimor.internal.resolve;

import st.networkers.rimor.command.CommandDefinition;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.MainInstruction;
import st.networkers.rimor.internal.command.Command;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;
import st.networkers.rimor.util.InspectionUtils;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandResolver {

    private CommandResolver() {
    }

    public static Command resolve(CommandDefinition command) {
        return resolve(null, command);
    }

    public static Command resolve(Command parent, CommandDefinition definition) {
        Command command = new Command(
                parent,
                definition,
                InspectionUtils.getAliases(definition.getClass()),
                ReflectionUtils.getMappedAnnotations(definition.getClass())
        );

        ResolvedInstructions resolvedInstructions = resolveInstructions(command);
        command.setMainInstruction(resolvedInstructions.getMainInstruction());
        resolvedInstructions.getInstructions().forEach(command::registerInstruction);

        resolveSubcommands(command).forEach(command::registerSubcommand);

        return command;
    }

    private static ResolvedInstructions resolveInstructions(Command command) {
        ResolvedInstructions results = new ResolvedInstructions();

        for (Method method : command.getCommandInstance().getClass().getMethods()) {
            ResolvedInstruction instruction = ResolvedInstruction.build(command, method,
                    InspectionUtils.getAliases(method));

            if (method.isAnnotationPresent(MainInstruction.class))
                results.setMainInstruction(instruction);

            if (method.isAnnotationPresent(Instruction.class))
                results.addInstruction(instruction);
        }

        return results;
    }

    private static List<Command> resolveSubcommands(Command command) {
        return command.getCommandInstance().getSubcommands().stream()
                .map(subcommand -> resolve(command, subcommand))
                .collect(Collectors.toList());
    }
}
