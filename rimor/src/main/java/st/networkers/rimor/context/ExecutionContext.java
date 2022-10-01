package st.networkers.rimor.context;

import st.networkers.rimor.internal.inject.Token;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExecutionContext implements Cloneable {

    private Map<Class<?>, List<ContextComponent>> components;

    public static ExecutionContext build(ContextComponent... components) {
        return build(Arrays.stream(components));
    }

    public static ExecutionContext build(Collection<ContextComponent> components) {
        return build(components.stream());
    }

    public static ExecutionContext build(Stream<ContextComponent> components) {
        return new ExecutionContext(components
                .collect(Collectors.groupingBy(ContextComponent::getType))
        );
    }

    public ExecutionContext(Map<Class<?>, List<ContextComponent>> components) {
        this.components = components;
    }

    public Optional<Object> get(Token token) {
        if (!this.components.containsKey(token.getType()))
            return Optional.empty();

        return this.components.get(token.getType()).stream()
                .filter(component -> component.canProvide(token))
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
