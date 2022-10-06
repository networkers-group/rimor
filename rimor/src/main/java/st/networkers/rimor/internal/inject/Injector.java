package st.networkers.rimor.internal.inject;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.provide.ProviderRegistry;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.RimorProviderWrapper;
import st.networkers.rimor.util.ReflectionUtils;

public class Injector {

    @Getter private final ProviderRegistry providerRegistry;

    public Injector() {
        this(new ProviderRegistry());
    }

    private Injector(ProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    public Injector registerProviders(RimorProviderWrapper... wrappers) {
        providerRegistry.register(wrappers);
        return this;
    }

    public <T> T get(Token<T> token, ExecutionContext context) {
        return context.get(token).orElseGet(
                // Java 8 optionals don't have #or 😣
                () -> providerRegistry.provide(token, context, this).orElse(null)
        );
    }

    public Object invokeMethod(CachedMethod cachedMethod, Object instance, ExecutionContext context) {
        return ReflectionUtils.invoke(cachedMethod.getMethod(), instance, resolveParameters(cachedMethod, context));
    }

    public Object[] resolveParameters(CachedMethod cachedMethod, ExecutionContext context) {
        Object[] parameters = new Object[cachedMethod.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : cachedMethod.getParameters()) {
            parameters[i++] = get(new Token<>(TypeToken.of(parameter.getType()), parameter.getAnnotationsMap()), context);
        }

        return parameters;
    }
}
