package st.networkers.rimor.instruction;

import st.networkers.rimor.execute.Executable;
import st.networkers.rimor.reflect.CachedMethod;

import java.util.Collection;
import java.util.Objects;

public class Instruction implements Executable {

    private final Object commandInstance;
    private final CachedMethod method;

    private final Collection<String> identifiers;

    public Instruction(Object commandInstance, CachedMethod method, Collection<String> identifiers) {
        this.commandInstance = commandInstance;
        this.method = method;
        this.identifiers = identifiers;
    }

    public Object getCommandInstance() {
        return commandInstance;
    }

    public CachedMethod getMethod() {
        return method;
    }

    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruction)) return false;
        Instruction that = (Instruction) o;
        return Objects.equals(commandInstance, that.commandInstance) && Objects.equals(method, that.method) && Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandInstance, method, identifiers);
    }
}
