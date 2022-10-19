package st.networkers.rimor.internal.provide;

import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.RimorProvider;

import java.util.Optional;

public interface ProviderRegistry {

    /**
     * Registers the given {@link RimorProvider}s.
     *
     * @param provider the provider to register
     */
    <T> void register(RimorProvider<T> provider);

    /**
     * Finds the {@link RimorProvider} associated with the given {@link Token}.
     *
     * @param token the token to get its associated provider
     * @return an optional containing the provider associated, or empty if none was found.
     */
    <T> Optional<RimorProvider<T>> findFor(Token<T> token);
}
