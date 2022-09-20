package st.networkers.rimor.internal.command;

import lombok.Getter;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.reflect.CachedMethod;

import java.lang.reflect.Method;
import java.util.List;

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
        this.aliases = aliases;
    }

    public Command getCommandInstance() {
        return command.getCommandInstance();
    }
}
