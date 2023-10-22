package st.networkers.rimor.context.provide;

import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.util.MatchingMap;
import st.networkers.rimor.util.OptionalUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

public class ExecutionContextProviderRegistry implements Cloneable {

    // TODO cache
    private MatchingMap<Token, ExecutionContextProvider<?>> providers = new MatchingMap<>();

    /**
     * Registers the given {@link ExecutionContextProvider}s.
     *
     * @param provider the provider to register
     */
    public void register(ExecutionContextProvider<?> provider) {
        for (Type type : provider.getProvidedTypes()) {
            Token token = Token.of(type, provider.getQualifiersMap(), provider.getRequiredQualifiers());

            this.getFor(token).ifPresent(existingProvider -> {
                String message = String.format("found several execution context providers with matching tokens " +
                                               "at the same level: %s, %s", existingProvider, provider);
                throw new IllegalStateException(message);
            });
            this.providers.put(token, provider);
        }
    }

    /**
     * Finds the {@link ExecutionContextProvider} bound to the given {@link Token} or, if there are no bindings for it,
     * finds any suitable provider for its type and annotations, if able.
     *
     * @param token the token to get its bound (or a suitable) provider
     * @return an optional containing the found provider, or empty if none was found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<ExecutionContextProvider<T>> findFor(Token token) {
        return OptionalUtils.firstPresent(
                        this.getFor(token),
                        () -> this.findAnySuitable(token)
                )
                .map(provider -> (ExecutionContextProvider<T>) provider);
    }

    private Optional<ExecutionContextProvider<?>> getFor(Token token) {
        return Optional.ofNullable(this.providers.get(token));
    }

    private Optional<ExecutionContextProvider<?>> findAnySuitable(Token token) {
        for (Entry<Integer, List<Entry<Token, ExecutionContextProvider<?>>>> matchingTokenEntry : this.providers.getDelegate().entrySet()) {
            for (Entry<Token, ExecutionContextProvider<?>> entry : matchingTokenEntry.getValue()) {
                if (token.isAssignableFrom(entry.getKey()))
                    return Optional.ofNullable(entry.getValue());
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionContextProviderRegistry)) return false;
        ExecutionContextProviderRegistry that = (ExecutionContextProviderRegistry) o;
        return Objects.equals(providers, that.providers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providers);
    }

    @Override
    public ExecutionContextProviderRegistry clone() {
        try {
            ExecutionContextProviderRegistry clone = (ExecutionContextProviderRegistry) super.clone();
            clone.providers = new MatchingMap<>(this.providers);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
