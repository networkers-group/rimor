package st.networkers.rimor.util;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.provide.ParameterProviderRegistry;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;

public final class InjectionUtils {

    private InjectionUtils() {
    }

    public static Object invokeMethod(CachedMethod cachedMethod,
                                      Object instance,
                                      ExecutionContext context,
                                      ParameterProviderRegistry parameterProviderRegistry) {
        return ReflectionUtils.invoke(
                cachedMethod.getMethod(),
                instance,
                resolveParameters(cachedMethod, context, parameterProviderRegistry)
        );
    }

    public static Object[] resolveParameters(CachedMethod cachedMethod,
                                             ExecutionContext context,
                                             ParameterProviderRegistry parameterProviderRegistry) {
        Object[] parameters = new Object[cachedMethod.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : cachedMethod.getParameters()) {
            parameters[i++] = resolveParameter(parameter, context, parameterProviderRegistry);
        }

        return parameters;
    }

    private static Object resolveParameter(CachedParameter parameter,
                                           ExecutionContext context,
                                           ParameterProviderRegistry parameterProviderRegistry) {
        // Java 8 optionals don't have #or 😣
        return context.get(parameter)
                .orElseGet(() -> parameterProviderRegistry.provide(parameter, context)
                .orElse(null));
    }
}
