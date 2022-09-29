package st.networkers.rimor.internal.provide;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ParameterProviderWrapper;
import st.networkers.rimor.provide.ProvidesParameter;

import java.lang.reflect.Method;
import java.util.*;

public class ParameterProviderRegistry {

    private final Map<Class<?>, List<ParameterProvider>> parameterProviders = new HashMap<>();

    public void register(ParameterProviderWrapper... wrappers) {
        for (ParameterProviderWrapper wrapper : wrappers) {
            for (Method method : wrapper.getClass().getMethods()) {
                if (!method.isAnnotationPresent(ProvidesParameter.class))
                    continue;

                CachedMethod cachedMethod = CachedMethod.build(method);
                this.register(method.getReturnType(), new ParameterProvider(wrapper, cachedMethod));
            }
        }
    }

    private void register(Class<?> type, ParameterProvider provider) {
        this.parameterProviders.computeIfAbsent(type, t -> new ArrayList<>()).add(provider);
    }

    public Optional<Object> provide(CachedParameter parameter, ExecutionContext context) {
        return this.findFor(parameter).map(provider -> provider.get(parameter, context, this));
    }

    public Optional<ParameterProvider> findFor(CachedParameter parameter) {
        return parameterProviders.get(parameter.getType()).stream()
                .filter(provider -> provider.canProvide(parameter))
                .findAny();
    }
}
