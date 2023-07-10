package st.networkers.rimor.inject;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.provide.RimorProvider;
import st.networkers.rimor.reflect.CachedMethod;

import java.util.Optional;

/**
 * Provides objects bound to {@link Token}s from an {@link ExecutionContext}, or from a registered
 * {@link RimorProvider} if the context does not have any bindings for that token.
 * <p>
 * To register a {@link RimorProvider}, see {@link Rimor#registerProvider(RimorProvider)}.
 */
public interface RimorInjector {

    /**
     * Gets an {@link Optional} wrapping the object bound to the given {@link Token} from the given
     * {@link ExecutionContext}, if able. Otherwise, gets it from a registered {@link RimorProvider}, or returns an empty
     * optional if there are no available providers for the given token.
     *
     * @param token   the token to get its bound object
     * @param context the context of a command execution
     * @return an {@link Optional} wrapping the object bound to the token, or empty
     */
    <T> Optional<T> get(Token<T> token, ExecutionContext context);

    /**
     * Invokes the given method injecting all its parameters from the provided {@link ExecutionContext} or a registered
     * {@link RimorProvider}.
     *
     * @param cachedMethod the method to invoke
     * @param instance     an instance of the method's class to invoke it on, or {@code null} if static
     * @param context      the context of a command execution
     * @return the result of executing the method
     */
    Object invokeMethod(CachedMethod cachedMethod, Object instance, ExecutionContext context);
}
