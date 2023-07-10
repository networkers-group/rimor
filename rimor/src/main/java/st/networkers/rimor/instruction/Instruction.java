package st.networkers.rimor.instruction;

import st.networkers.rimor.execute.Executable;
import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.annotated.AnnotatedProperties;
import st.networkers.rimor.reflect.CachedMethod;

import java.util.Collection;
import java.util.Objects;

public class Instruction implements Annotated, Executable {

    private final Object commandInstance;
    private final CachedMethod method;

    private final AnnotatedProperties annotatedProperties;
    private final Collection<String> identifiers;

    public Instruction(Object commandInstance, CachedMethod method,
                       AnnotatedProperties annotatedProperties, Collection<String> identifiers) {
        this.commandInstance = commandInstance;
        this.method = method;
        this.annotatedProperties = annotatedProperties;
        this.identifiers = identifiers;
    }

    public Object getCommandInstance() {
        return commandInstance;
    }

    public CachedMethod getMethod() {
        return method;
    }

    @Override
    public AnnotatedProperties getAnnotatedProperties() {
        return annotatedProperties;
    }

    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(commandInstance, that.commandInstance) && Objects.equals(method, that.method) && Objects.equals(annotatedProperties, that.annotatedProperties) && Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandInstance, method, annotatedProperties, identifiers);
    }
}
