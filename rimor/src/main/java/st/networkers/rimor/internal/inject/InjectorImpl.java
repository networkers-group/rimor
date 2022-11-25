package st.networkers.rimor.internal.inject;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.util.OptionalUtils;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Optional;

public class InjectorImpl implements Injector {

    private final ProviderRegistry providerRegistry;

    public InjectorImpl(ProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    @Override
    public <T> Optional<T> get(Token<T> token, ExecutionContext context) {
        return OptionalUtils.firstPresent(
                context.get(token),
                () -> providerRegistry.findFor(token, this, context)
                        .map(provider -> provider.get(token, this, context))
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
            Token<?> token = new Token<>(parameter.getType(), parameter.getAnnotationsMap());
            parameters[i++] = this.get(token, context).orElse(null);
        }

        return parameters;
    }
}
