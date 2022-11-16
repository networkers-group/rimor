package st.networkers.rimor.internal.instruction;

import lombok.Getter;
import st.networkers.rimor.command.CommandDefinition;
import st.networkers.rimor.internal.command.ResolvedCommand;
import st.networkers.rimor.internal.inject.AbstractAnnotated;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ResolvedInstruction extends AbstractAnnotated<ResolvedInstruction> {

    public static ResolvedInstruction build(ResolvedCommand command, Method method, Collection<String> aliases) {
        return new ResolvedInstruction(command, CachedMethod.build(method), aliases, ReflectionUtils.getMappedAnnotations(method));
    }

    private final ResolvedCommand command;
    private final CachedMethod method;
    private final Collection<String> aliases;

    public ResolvedInstruction(ResolvedCommand command,
                               CachedMethod method,
                               Collection<String> aliases,
                               Map<Class<? extends Annotation>, Annotation> annotations) {
        super(annotations);
        this.command = command;
        this.method = method;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public CommandDefinition getCommandInstance() {
        return command.getCommandInstance();
    }
}
