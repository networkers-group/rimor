package st.networkers.rimor.provide;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;

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
    <T> Optional<RimorProvider<T>> findFor(Token<T> token, ExecutionContext context);
}
