package st.networkers.rimor.internal.resolve;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.MainInstruction;
import st.networkers.rimor.internal.command.ResolvedCommand;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;
import st.networkers.rimor.util.InspectionUtils;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandResolver {

    private CommandResolver() {
    }

    public static ResolvedCommand resolve(Command command) {
        return resolve(null, command);
    }

    public static ResolvedCommand resolve(ResolvedCommand parent, Command command) {
        ResolvedCommand resolvedCommand = new ResolvedCommand(
                parent,
                command,
                InspectionUtils.getAliases(command.getClass()),
                ReflectionUtils.getMappedAnnotations(command.getClass())
        );

        ResolvedInstructions resolvedInstructions = resolveInstructions(resolvedCommand);
        resolvedCommand.setMainInstruction(resolvedInstructions.getMainInstruction());
        resolvedInstructions.getInstructions().forEach(resolvedCommand::registerInstruction);

        resolveSubcommands(resolvedCommand).forEach(resolvedCommand::registerSubcommand);

        return resolvedCommand;
    }

    private static ResolvedInstructions resolveInstructions(ResolvedCommand command) {
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

    private static List<ResolvedCommand> resolveSubcommands(ResolvedCommand resolvedCommand) {
        return resolvedCommand.getCommandInstance().getSubcommands().stream()
                .map(subcommand -> resolve(resolvedCommand, subcommand))
                .collect(Collectors.toList());
    }
}
