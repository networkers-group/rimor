package st.networkers.rimor.internal.instruction;

import lombok.Getter;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.command.RimorCommand;
import st.networkers.rimor.internal.reflect.CachedMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommandInstruction {

    public static CommandInstruction build(RimorCommand command, Method method, List<String> aliases) {
        return new CommandInstruction(command, CachedMethod.build(method), aliases);
    }

    private final RimorCommand command;
    private final CachedMethod method;
    private final List<String> aliases;

    public CommandInstruction(RimorCommand command, CachedMethod method, List<String> aliases) {
        this.command = command;
        this.method = method;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public Command getCommandInstance() {
        return command.getCommandInstance();
    }
}
