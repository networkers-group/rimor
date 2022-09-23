package st.networkers.rimor.internal;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.instruction.IgnoreMethodName;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.instruction.MainInstruction;
import st.networkers.rimor.internal.command.RimorCommand;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.util.InspectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandResolver {

    private CommandResolver() {
    }

    public static RimorCommand resolve(Command command) {
        return resolve(null, command);
    }

    public static RimorCommand resolve(RimorCommand parent, Command command) {
        RimorCommand rimorCommand = new RimorCommand(parent, command, resolveAliases(command));

        registerInstructions(rimorCommand);
        registerSubcommands(rimorCommand);

        return rimorCommand;
    }

    private static void registerInstructions(RimorCommand command) {
        for (Method method : command.getCommandInstance().getClass().getMethods()) {
            if (method.isAnnotationPresent(MainInstruction.class)) {
                command.registerMainInstruction(CommandInstruction.build(command, method, null));
                continue;
            }

            if (method.isAnnotationPresent(Instruction.class)) {
                List<String> aliases = new ArrayList<>(InspectionUtils.getAliases(method));

                if (aliases.isEmpty() || !method.isAnnotationPresent(IgnoreMethodName.class))
                    aliases.add(method.getName());

                command.registerInstruction(CommandInstruction.build(command, method, aliases));
            }
        }
    }

    private static void registerSubcommands(RimorCommand command) {
        for (RimorCommand subcommand : resolveSubcommands(command))
            command.registerSubcommand(subcommand);
    }

    private static List<String> resolveAliases(Command command) {
        List<String> aliases = InspectionUtils.getAliases(command.getClass());

        if (aliases.isEmpty())
            return Collections.singletonList(command.getClass().getSimpleName());

        return aliases;
    }

    private static List<RimorCommand> resolveSubcommands(RimorCommand rimorCommand) {
        return rimorCommand.getCommandInstance().getSubcommands().stream()
                .map(subcommand -> resolve(rimorCommand, subcommand))
                .collect(Collectors.toList());
    }
}
