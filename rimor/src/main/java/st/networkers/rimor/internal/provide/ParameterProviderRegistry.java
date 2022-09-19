package st.networkers.rimor.internal.provide;

import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.provide.ProvidesParameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterProviderRegistry {

    private final Map<Class<?>, List<ParameterProvider>> parameterProviders = new HashMap<>();

    public void registerAll(Object... instances) {
        for (Object object : instances)
            this.register(object);
    }

    public void register(Object object) {
        for (Method method : object.getClass().getMethods()) {
            if (!method.isAnnotationPresent(ProvidesParameter.class))
                continue;

            CachedMethod cachedMethod = CachedMethod.build(method);
            this.register(method.getReturnType(), new ParameterProvider(object, cachedMethod));
        }
    }

    private void register(Class<?> type, ParameterProvider provider) {
        this.parameterProviders.computeIfAbsent(type, t -> new ArrayList<>()).add(provider);
    }

    public List<ParameterProvider> get(Class<?> type) {
        return parameterProviders.get(type);
    }
}
