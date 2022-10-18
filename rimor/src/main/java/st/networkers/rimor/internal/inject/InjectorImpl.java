package st.networkers.rimor.internal.inject;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.provide.ProviderRegistry;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.util.ReflectionUtils;

public class InjectorImpl implements Injector {

    private final ProviderRegistry providerRegistry;

    public InjectorImpl(ProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    @Override
    public <T> T get(Token<T> token, ExecutionContext context) {
        // Java 8 optionals don't have #or 😣
        return context.get(token).orElseGet(
                () -> providerRegistry.findFor(token).map(provider -> provider.get(token, this, context)).orElse(null)
        );
    }

    @Override
    public Object invokeMethod(CachedMethod cachedMethod, Object instance, ExecutionContext context) {
        return ReflectionUtils.invoke(cachedMethod.getMethod(), instance, resolveParameters(cachedMethod, context));
    }

    private Object[] resolveParameters(CachedMethod cachedMethod, ExecutionContext context) {
        Object[] parameters = new Object[cachedMethod.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : cachedMethod.getParameters()) {
            parameters[i++] = this.get(new Token<>(parameter.getType(), parameter.getAnnotationsMap()), context);
        }

        return parameters;
    }
}
