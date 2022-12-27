package st.networkers.rimor.inject;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.provide.RimorProvider;

import java.util.Optional;

/**
 * Provides an object associated with a given {@link Token} from an {@link ExecutionContext}, or from a registered
 * {@link RimorProvider} if the context is not able to provide anything for the token.
 * <p>
 * To register a {@link RimorProvider}, see {@link Rimor#registerProvider(RimorProvider)}.
 */
public interface Injector {

    /**
     * Gets an {@link Optional} wrapping the object associated with the given {@link Token} from the given
     * {@link ExecutionContext}, if able. Otherwise, gets it from a registered provider, or an empty optional if there
     * are no providers that provide the given token.
     *
     * @param token   the token to get its associated object
     * @param context the context of a command execution
     * @return an {@link Optional} wrapping the object associated with the token, or empty
     */
    <T> Optional<T> get(Token<T> token, ExecutionContext context);

    /**
     * Invokes the given method injecting all its parameters.
     *
     * @param cachedMethod the method to invoke
     * @param instance     an instance of the method's class to invoke it on, or {@code null} if static
     * @param context      the context of a command execution
     * @return the result of executing the method
     */
    Object invokeMethod(CachedMethod cachedMethod, Object instance, ExecutionContext context);

    ProviderRegistry getProviderRegistry();
}
