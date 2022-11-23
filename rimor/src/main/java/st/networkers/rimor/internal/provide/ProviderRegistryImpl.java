package st.networkers.rimor.internal.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.provide.RimorProvider;

import java.util.*;
import java.util.stream.Stream;

public class ProviderRegistryImpl implements ProviderRegistry {

    private final Map<TypeToken<?>, List<RimorProvider<?>>> providers = new HashMap<>();

    @Override
    public <T> void register(RimorProvider<T> provider) {
        for (TypeToken<? extends T> type : provider.getProvidedTypes())
            this.providers.computeIfAbsent(type, t -> new ArrayList<>()).add(provider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<RimorProvider<T>> findFor(Token<T> token, Injector injector, ExecutionContext context) {
        return this.get(token)
                .filter(provider -> provider.canProvide(token, injector, context))
                .map(provider -> (RimorProvider<T>) provider)
                .findAny();
    }

    private <T> Stream<RimorProvider<?>> get(Token<T> token) {
        return providers.containsKey(token.getType())
                ? providers.get(token.getType()).stream()
                : providers.values().stream().flatMap(Collection::stream);
    }
}
