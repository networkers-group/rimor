package st.networkers.rimor.instruction;

import st.networkers.rimor.Executable;
import st.networkers.rimor.command.MappedCommand;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.inject.Annotated;
import st.networkers.rimor.inject.AnnotatedProperties;
import st.networkers.rimor.reflect.CachedMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class Instruction implements Annotated, Executable {

    public static Instruction build(MappedCommand command, Method method, Collection<String> aliases) {
        return new Instruction(
                command,
                CachedMethod.build(method),
                aliases.stream().map(String::toLowerCase).collect(Collectors.toList()),
                AnnotatedProperties.build(method)
        );
    }

    private final MappedCommand command;
    private final CachedMethod method;
    private final Collection<String> aliases;
    private final AnnotatedProperties annotatedProperties;

    private Instruction(MappedCommand command,
                        CachedMethod method,
                        Collection<String> aliases,
                        AnnotatedProperties annotatedProperties) {
        this.command = command;
        this.method = method;
        this.aliases = aliases;
        this.annotatedProperties = annotatedProperties;
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
    public AnnotatedProperties getAnnotatedProperties() {
        return annotatedProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(command, that.command) && Objects.equals(method, that.method) && Objects.equals(aliases, that.aliases) && Objects.equals(annotatedProperties, that.annotatedProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, method, aliases, annotatedProperties);
    }
}
