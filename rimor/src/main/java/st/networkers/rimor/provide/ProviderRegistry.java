package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;

import java.util.*;
import java.util.stream.Stream;

public class ProviderRegistry implements Cloneable {

    private Map<TypeToken<?>, List<RimorProvider<?>>> providers = new HashMap<>();

    /**
     * Registers the given {@link RimorProvider}s.
     *
     * @param provider the provider to register
     */
    public <T> void register(RimorProvider<T> provider) {
        for (TypeToken<? extends T> type : provider.getProvidedTypes())
            this.providers.computeIfAbsent(type, t -> new ArrayList<>()).add(provider);
    }

    /**
     * Finds the {@link RimorProvider} associated with the given {@link Token}.
     *
     * @param token the token to get its associated provider
     * @return an optional containing the provider associated, or empty if none was found.
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<RimorProvider<T>> findFor(Token<T> token, ExecutionContext context) {
        return this.get(token)
                .filter(provider -> provider.canProvide(token, context))
                .map(provider -> (RimorProvider<T>) provider)
                .findAny();
    }

    private <T> Stream<RimorProvider<?>> get(Token<T> token) {
        return providers.containsKey(token.getType())
                ? providers.get(token.getType()).stream()
                : providers.values().stream().flatMap(Collection::stream);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProviderRegistry)) return false;
        ProviderRegistry that = (ProviderRegistry) o;
        return Objects.equals(providers, that.providers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providers);
    }

    @Override
    public ProviderRegistry clone() {
        try {
            ProviderRegistry clone = (ProviderRegistry) super.clone();
            clone.providers = new HashMap<>(this.providers);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
