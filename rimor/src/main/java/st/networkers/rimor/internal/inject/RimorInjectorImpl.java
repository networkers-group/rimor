package st.networkers.rimor.internal.inject;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;
import st.networkers.rimor.util.OptionalUtils;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Optional;

public class RimorInjectorImpl implements RimorInjector {

    private final ProviderRegistry providerRegistry;

    public RimorInjectorImpl() {
        this(new ProviderRegistry());
    }

    public RimorInjectorImpl(ProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    @Override
    public <T> Optional<T> get(Token<T> token, ExecutionContext context) {
        return OptionalUtils.firstPresent(
                context.get(token),
                () -> providerRegistry.findFor(token).map(provider -> provider.get(token, context))
        );
    }

    @Override
    public Object invokeMethod(CachedMethod method, Object instance, ExecutionContext context) {
        return ReflectionUtils.invoke(method.getMethod(), instance, resolveParameters(method, context));
    }

    @Override
    public ProviderRegistry getProviderRegistry() {
        return providerRegistry;
    }

    private Object[] resolveParameters(CachedMethod method, ExecutionContext context) {
        Object[] parameters = new Object[method.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : method.getParameters()) {
            parameters[i++] = this.get(ParameterToken.build(method, parameter), context).orElse(null);
        }

        return parameters;
    }
}
