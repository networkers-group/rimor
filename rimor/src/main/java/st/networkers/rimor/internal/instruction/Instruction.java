package st.networkers.rimor.internal.instruction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import st.networkers.rimor.Executable;
import st.networkers.rimor.command.CommandDefinition;
import st.networkers.rimor.internal.command.Command;
import st.networkers.rimor.internal.inject.AbstractAnnotated;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Instruction extends AbstractAnnotated<Instruction> implements Executable {

    public static Instruction build(Command command, Method method, Collection<String> aliases) {
        return new Instruction(command, CachedMethod.build(method), aliases, ReflectionUtils.getMappedAnnotations(method));
    }

    private final Command command;
    private final CachedMethod method;
    private final Collection<String> aliases;

    public Instruction(Command command,
                       CachedMethod method,
                       Collection<String> aliases,
                       Map<Class<? extends Annotation>, Annotation> annotations) {
        super(annotations);
        this.command = command;
        this.method = method;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public CommandDefinition getCommandInstance() {
        return command.getDefinition();
    }
}
