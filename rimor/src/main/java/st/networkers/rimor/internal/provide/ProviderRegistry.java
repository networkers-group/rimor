package st.networkers.rimor.internal.provide;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.provide.RimorProviderWrapper;
import st.networkers.rimor.provide.ProvidesParameter;

import java.lang.reflect.Method;
import java.util.*;

public class ProviderRegistry {

    private final Map<Class<?>, List<Provider>> providers = new HashMap<>();

    public void register(RimorProviderWrapper... wrappers) {
        for (RimorProviderWrapper wrapper : wrappers) {
            for (Method method : wrapper.getClass().getMethods()) {
                if (!method.isAnnotationPresent(ProvidesParameter.class))
                    continue;

                CachedMethod cachedMethod = CachedMethod.build(method);
                this.register(method.getReturnType(), Provider.build(wrapper, cachedMethod));
            }
        }
    }

    public void register(Class<?> type, Provider provider) {
        this.providers.computeIfAbsent(type, t -> new ArrayList<>()).add(provider);
    }

    public <T> Optional<T> provide(Token<? super T> token, ExecutionContext context, Injector injector) {
        return this.findFor(token).map(provider -> provider.get(token, context, injector));
    }

    public Optional<Provider> findFor(Token token) {
        return providers.containsKey(token.getType())
                ? providers.get(token.getType()).stream()
                .filter(provider -> provider.canProvide(token))
                .findAny()

                : providers.values().stream()
                .flatMap(Collection::stream)
                .filter(provider -> provider.canProvide(token))
                .findAny();
    }
}
