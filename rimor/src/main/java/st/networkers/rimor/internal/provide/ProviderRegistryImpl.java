package st.networkers.rimor.internal.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.RimorProvider;

import java.util.*;
import java.util.stream.Stream;

public class ProviderRegistryImpl implements ProviderRegistry {

    private final Map<TypeToken<?>, List<RimorProvider<?>>> providers = new HashMap<>();

    @Override
    public void register(RimorProvider<?>... providers) {
        for (RimorProvider<?> provider : providers)
            this.register(provider);
    }

    private <T> void register(RimorProvider<T> provider) {
        for (TypeToken<T> type : provider.getProvidedTypes())
            this.register(type, provider);
    }

    private <T> void register(TypeToken<T> type, RimorProvider<? super T> provider) {
        this.providers.computeIfAbsent(type, t -> new ArrayList<>()).add(provider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<RimorProvider<T>> findFor(Token<T> token) {
        return this.get(token)
                .filter(provider -> provider.canProvide(token))
                .map(provider -> (RimorProvider<T>) provider)
                .findAny();
    }

    private <T> Stream<RimorProvider<?>> get(Token<T> token) {
        return providers.containsKey(token.getType())
                ? providers.get(token.getType()).stream()
                : providers.values().stream().flatMap(Collection::stream);
    }
}
