package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.inject.Token;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains injectable information relative to the execution of a command (for example, its parameters).
 * <p>
 * Every piece of information is wrapped in a {@link ContextComponent}.
 */
public class ExecutionContext {

    private final Map<TypeToken<?>, List<ContextComponent<?>>> components;

    public static ExecutionContext build(ContextComponent<?>... components) {
        return build(Arrays.stream(components));
    }

    public static ExecutionContext build(Collection<ContextComponent<?>> components) {
        return build(components.stream());
    }

    public static ExecutionContext build(Stream<ContextComponent<?>> components) {
        return new ExecutionContext(components
                .collect(Collectors.groupingBy(ContextComponent::getType))
        );
    }

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
        ExecutionContext that = (ExecutionContext) o;
        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }
}
