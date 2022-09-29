package st.networkers.rimor.internal.inject;

import lombok.Getter;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.provide.ParameterProviderRegistry;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ParameterProviderWrapper;
import st.networkers.rimor.util.ReflectionUtils;

public class Injector {

    @Getter private final ParameterProviderRegistry parameterProviderRegistry;

    public Injector() {
        this(new ParameterProviderRegistry());
    }

    private Injector(ParameterProviderRegistry parameterProviderRegistry) {
        this.parameterProviderRegistry = parameterProviderRegistry;
    }

    public Injector registerParameterProviders(ParameterProviderWrapper... wrappers) {
        parameterProviderRegistry.register(wrappers);
        return this;
    }

    public Object invokeMethod(CachedMethod cachedMethod, Object instance, ExecutionContext context) {
        return ReflectionUtils.invoke(cachedMethod.getMethod(), instance, resolveParameters(cachedMethod, context));
    }

    public Object[] resolveParameters(CachedMethod cachedMethod, ExecutionContext context) {
        Object[] parameters = new Object[cachedMethod.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : cachedMethod.getParameters()) {
            parameters[i++] = resolveParameter(parameter, context);
        }

        return parameters;
    }

    public Object resolveParameter(CachedParameter parameter, ExecutionContext context) {
        // Java 8 optionals don't have #or 😣
        return context.get(parameter)
                .orElseGet(() -> parameterProviderRegistry.provide(parameter, context, this)
                        .orElse(null));
    }
}
