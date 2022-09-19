package st.networkers.rimor.internal.command.instruction;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.reflect.CachedMethod;

import java.lang.reflect.Method;
import java.util.List;

public class CommandInstruction {

    public static CommandInstruction build(Command commandInstance, Method method, List<String> aliases) {
        return new CommandInstruction(commandInstance, CachedMethod.build(method), aliases);
    }

    private final Command commandInstance;
    private final CachedMethod method;
    private final List<String> aliases;

    public CommandInstruction(Command commandInstance, CachedMethod method, List<String> aliases) {
        this.commandInstance = commandInstance;
        this.method = method;
        this.aliases = aliases;
    }

    public Command getCommandInstance() {
        return commandInstance;
    }

    public CachedMethod getMethod() {
        return method;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
