package st.networkers.rimor.context;

import st.networkers.rimor.internal.reflect.CachedParameter;

import java.util.*;
import java.util.stream.Collectors;

public class ExecutionContext implements Cloneable {

    private Map<Class<?>, List<ContextComponent>> components;

    public static ExecutionContext build(List<ContextComponent> components) {
        return new ExecutionContext(
                components.stream().collect(Collectors.groupingBy(ContextComponent::getType))
        );
    }

    public ExecutionContext(Map<Class<?>, List<ContextComponent>> components) {
        this.components = components;
    }

    public Optional<Object> get(CachedParameter parameter) {
        if (!this.components.containsKey(parameter.getType()))
            return Optional.empty();

        return this.components.get(parameter.getType()).stream()
                .filter(component -> component.canProvide(parameter))
                .map(ContextComponent::getObject)
                .findAny();
    }

    public void addComponent(ContextComponent component) {
        this.components.computeIfAbsent(component.getType(), t -> new ArrayList<>()).add(component);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionContext)) return false;
        ExecutionContext that = (ExecutionContext) o;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }

    @Override
    public ExecutionContext clone() {
        try {
            ExecutionContext clone = (ExecutionContext) super.clone();
            clone.components = new HashMap<>(this.components);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
