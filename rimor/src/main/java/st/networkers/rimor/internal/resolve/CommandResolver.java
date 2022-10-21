package st.networkers.rimor.internal.resolve;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.instruction.IgnoreMethodName;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.MainInstruction;
import st.networkers.rimor.internal.command.ResolvedCommand;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;
import st.networkers.rimor.util.InspectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandResolver {

    private CommandResolver() {
    }

    public static ResolvedCommand resolve(Command command) {
        return resolve(null, command);
    }

    public static ResolvedCommand resolve(ResolvedCommand parent, Command command) {
        ResolvedCommand resolvedCommand = new ResolvedCommand(parent, command, resolveAliases(command));

        ResolvedInstructions resolvedInstructions = resolveInstructions(resolvedCommand);
        resolvedInstructions.getMainInstructions().forEach(resolvedCommand::registerMainInstruction);
        resolvedInstructions.getInstructions().forEach(resolvedCommand::registerInstruction);

        resolveSubcommands(resolvedCommand).forEach(resolvedCommand::registerSubcommand);

        return resolvedCommand;
    }

    private static List<String> resolveAliases(Command command) {
        List<String> aliases = InspectionUtils.getAliases(command.getClass());

        if (aliases.isEmpty())
            return Collections.singletonList(command.getClass().getSimpleName());

        return aliases;
    }

    private static ResolvedInstructions resolveInstructions(ResolvedCommand command) {
        ResolvedInstructions instructions = new ResolvedInstructions();

        for (Method method : command.getCommandInstance().getClass().getMethods()) {
            if (method.isAnnotationPresent(MainInstruction.class))
                instructions.addMainInstruction(ResolvedInstruction.build(command, method, InspectionUtils.getAliases(method)));

            if (method.isAnnotationPresent(Instruction.class)) {
                List<String> aliases = new ArrayList<>(InspectionUtils.getAliases(method));
                if (aliases.isEmpty() || !method.isAnnotationPresent(IgnoreMethodName.class))
                    aliases.add(method.getName());

                instructions.addInstruction(ResolvedInstruction.build(command, method, aliases));
            }
        }

        return instructions;
    }

    private static List<ResolvedCommand> resolveSubcommands(ResolvedCommand resolvedCommand) {
        return resolvedCommand.getCommandInstance().getSubcommands().stream()
                .map(subcommand -> resolve(resolvedCommand, subcommand))
                .collect(Collectors.toList());
    }
}
