package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.inject.Token;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains injectable objects relative to the execution of a command (for example, its parameters, the executor...).
 * <p>
 * Every object is wrapped in a {@link ContextComponent}.
 */
public class ExecutionContext {

    public static ExecutionContext build(ContextComponent<?>... components) {
        return new ExecutionContext(Arrays.stream(components).collect(Collectors.groupingBy(ContextComponent::getType)));
    }

    public static ExecutionContext build(Collection<ContextComponent<?>> components) {
        return new ExecutionContext(components.stream().collect(Collectors.groupingBy(ContextComponent::getType)));
    }

    private final Map<TypeToken<?>, List<ContextComponent<?>>> components;

    private ExecutionContext(Map<TypeToken<?>, List<ContextComponent<?>>> components) {
        this.components = components;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Token<T> token) {
        if (!this.components.containsKey(token.getType()))
            return Optional.empty();

        return this.components.get(token.getType()).stream()
                .filter(component -> component.canProvide(token))
                .map(component -> (ContextComponent<T>) component)
                .map(ContextComponent::getObject)
                .findAny();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionContext)) return false;
        ExecutionContext context = (ExecutionContext) o;
        return Objects.equals(components, context.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }
}
