package st.networkers.rimor.util;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;

import java.util.function.Function;

public final class InjectionUtils {

    private InjectionUtils() {
    }

    public static Object[] resolve(CachedMethod cachedMethod,
                                   ExecutionContext context,
                                   Function<CachedParameter, Object> fallback) {
        Object[] parameters = new Object[cachedMethod.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : cachedMethod.getParameters()) {
            parameters[i++] = context.get(parameter).orElseGet(() -> fallback.apply(parameter));
        }

        return parameters;
    }
}
