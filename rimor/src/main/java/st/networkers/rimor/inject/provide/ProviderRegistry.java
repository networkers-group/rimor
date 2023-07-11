package st.networkers.rimor.inject.provide;

import st.networkers.rimor.inject.Token;
import st.networkers.rimor.util.MatchingMap;
import st.networkers.rimor.util.OptionalUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

public class ProviderRegistry implements Cloneable {

    // TODO cache
    private MatchingMap<Token<?>, RimorProvider<?>> providers = new MatchingMap<>();

    /**
     * Registers the given {@link RimorProvider}s.
     *
     * @param provider the provider to register
     */
    public void register(RimorProvider<?> provider) {
        for (Type type : provider.getProvidedTypes()) {
            Token<?> token = Token.of(type, provider.getAnnotationsMap(), provider.getRequiredAnnotations());
            this.providers.put(token, provider); // TODO throw if key already present?
        }
    }

    /**
     * Finds the {@link RimorProvider} bound to the given {@link Token} or, if there are no bindings for it,
     * finds any suitable provider for its type and annotations, if able.
     *
     * @param token the token to get its bound (or a suitable) provider
     * @return an optional containing the found provider, or empty if none was found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<RimorProvider<T>> findFor(Token<? super T> token) {
        return OptionalUtils.firstPresent(
                        this.getFor(token),
                        () -> this.findAnySuitable(token)
                )
                .map(provider -> (RimorProvider<T>) provider);
    }

    private Optional<RimorProvider<?>> getFor(Token<?> token) {
        return Optional.ofNullable(this.providers.get(token));
    }

    private Optional<RimorProvider<?>> findAnySuitable(Token<?> token) {
        for (Entry<Integer, List<Entry<Token<?>, RimorProvider<?>>>> matchingTokenEntry : this.providers.getDelegate().entrySet()) {
            for (Entry<Token<?>, RimorProvider<?>> entry : matchingTokenEntry.getValue()) {
                if (token.isAssignableFrom(entry.getKey()))
                    return Optional.ofNullable(entry.getValue());
            }
        }
        return Optional.empty();
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
            clone.providers = new MatchingMap<>(this.providers);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
