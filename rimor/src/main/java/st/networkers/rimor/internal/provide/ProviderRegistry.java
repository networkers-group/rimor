package st.networkers.rimor.internal.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.RimorProvider;

import java.util.*;
import java.util.stream.Stream;

public class ProviderRegistry {

    private final Map<TypeToken<?>, List<RimorProvider<?>>> providers = new HashMap<>();

    public void register(RimorProvider<?>... providers) {
        for (RimorProvider<?> provider : providers)
            this.register(provider);
    }

    private <T> void register(RimorProvider<T> provider) {
        for (TypeToken<T> type : provider.getProvidedTypes())
            this.register(type, provider);
    }

    public <T> void register(TypeToken<T> type, RimorProvider<? super T> provider) {
        this.providers.computeIfAbsent(type, t -> new ArrayList<>()).add(provider);
    }

    public <T> Optional<T> provide(Token<T> token, ExecutionContext context, Injector injector) {
        return this.findFor(token).map(provider -> provider.get(token, injector, context));
    }

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