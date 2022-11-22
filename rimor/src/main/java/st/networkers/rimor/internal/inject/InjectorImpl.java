package st.networkers.rimor.internal.inject;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Optional;

public class InjectorImpl implements Injector {

    private final ProviderRegistry providerRegistry;

    public InjectorImpl(ProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    @Override
    public <T> T get(Token<T> token, ExecutionContext context) {
        // Java 8 optionals don't have #or 😣
        return context.get(token).orElseGet(
                () -> providerRegistry.findFor(token, this, context)
                        .map(provider -> provider.get(token, this, context))
                        .orElse(null)
        );
    }

    @Override
    public <T> Optional<T> getOptional(Token<T> token, ExecutionContext context) {
        return Optional.ofNullable(this.get(token, context));
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
