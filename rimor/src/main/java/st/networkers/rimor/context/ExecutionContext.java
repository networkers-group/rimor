package st.networkers.rimor.context;

import st.networkers.rimor.internal.reflect.CachedParameter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExecutionContext {

    private final Map<Class<?>, List<ContextComponent>> components;

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
}
