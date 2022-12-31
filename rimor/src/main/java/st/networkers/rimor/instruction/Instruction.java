package st.networkers.rimor.instruction;

import st.networkers.rimor.Executable;
import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.inject.AbstractAnnotated;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Instruction extends AbstractAnnotated<Instruction> implements Executable {

    public static Instruction build(MappedCommand command, Method method, Collection<String> aliases) {
        return new Instruction(command, CachedMethod.build(method), aliases, ReflectionUtils.getMappedAnnotations(method));
    }

    private final MappedCommand command;
    private final CachedMethod method;
    private final Collection<String> aliases;

    public Instruction(MappedCommand command,
                       CachedMethod method,
                       Collection<String> aliases,
                       Map<Class<? extends Annotation>, Annotation> annotations) {
        super(annotations);
        this.command = command;
        this.method = method;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public RimorCommand getCommandInstance() {
        return command.getCommand();
    }

    public MappedCommand getCommand() {
        return command;
    }

    public CachedMethod getMethod() {
        return method;
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        if (!super.equals(o)) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(command, that.command) && Objects.equals(method, that.method) && Objects.equals(aliases, that.aliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), command, method, aliases);
    }
}
