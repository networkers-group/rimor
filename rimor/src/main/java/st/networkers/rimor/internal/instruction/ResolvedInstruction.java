package st.networkers.rimor.internal.instruction;

import lombok.Getter;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.internal.command.ResolvedCommand;
import st.networkers.rimor.internal.reflect.CachedMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class ResolvedInstruction {

    public static ResolvedInstruction build(ResolvedCommand command, Method method, Collection<String> aliases) {
        return new ResolvedInstruction(command, CachedMethod.build(method), aliases);
    }

    private final ResolvedCommand command;
    private final CachedMethod method;
    private final Collection<String> aliases;

    public ResolvedInstruction(ResolvedCommand command, CachedMethod method, Collection<String> aliases) {
        this.command = command;
        this.method = method;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public Command getCommandInstance() {
        return command.getCommandInstance();
    }
}
