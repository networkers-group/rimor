package st.networkers.rimor.internal;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.command.instruction.IgnoreMethodName;
import st.networkers.rimor.command.instruction.Instruction;
import st.networkers.rimor.command.instruction.MainInstruction;
import st.networkers.rimor.internal.command.RimorCommand;
import st.networkers.rimor.internal.command.instruction.CommandInstruction;
import st.networkers.rimor.util.InspectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static st.networkers.rimor.internal.command.RimorCommand.Instructions;

public final class CommandResolver {

    private CommandResolver() {
    }

    public static RimorCommand resolve(Command command) {
        return resolve(null, command);
    }

    public static RimorCommand resolve(RimorCommand parent, Command command) {
        RimorCommand rimorCommand = new RimorCommand(parent, command, resolveAliases(command), resolveInstructions(command));

        for (RimorCommand subcommand : resolveSubcommands(rimorCommand))
            rimorCommand.registerSubcommand(subcommand);

        return rimorCommand;
    }

    private static List<String> resolveAliases(Command command) {
        List<String> aliases = InspectionUtils.getAliases(command.getClass());

        if (aliases.isEmpty())
            aliases.add(command.getClass().getSimpleName());

        return aliases;
    }

    private static Instructions resolveInstructions(Command command) {
        Instructions cache = new Instructions();

        for (Method method : command.getClass().getMethods()) {
            if (method.isAnnotationPresent(MainInstruction.class)) {
                cache.cacheMainInstruction(CommandInstruction.build(command, method, null));
                continue;
            }

            if (method.isAnnotationPresent(Instruction.class)) {
                List<String> aliases = new ArrayList<>(InspectionUtils.getAliases(method));

                if (aliases.isEmpty() || !method.isAnnotationPresent(IgnoreMethodName.class))
                    aliases.add(method.getName());

                cache.cacheInstruction(CommandInstruction.build(command, method, aliases));
            }
        }

        return cache;
    }

    private static List<RimorCommand> resolveSubcommands(RimorCommand rimorCommand) {
        return rimorCommand.getCommand().getSubcommands().stream()
                .map(subcommand -> resolve(rimorCommand, subcommand))
                .collect(Collectors.toList());
    }
}
