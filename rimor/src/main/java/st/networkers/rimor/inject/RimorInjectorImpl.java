package st.networkers.rimor.inject;

import st.networkers.rimor.inject.provide.ProviderRegistry;
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

    private Object[] resolveParameters(CachedMethod method, ExecutionContext context) {
        Object[] parameters = new Object[method.getParameters().size()];

        int i = 0;
        for (CachedParameter parameter : method.getParameters()) {
            parameters[i++] = this.get(ParameterToken.build(method, parameter), context).orElse(null);
        }

        return parameters;
    }
}