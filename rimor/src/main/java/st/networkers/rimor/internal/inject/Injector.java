package st.networkers.rimor.internal.inject;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedMethod;

public interface Injector {

    /**
     * Gets the object for the given {@link Token} from the given {@link ExecutionContext}, if able. Otherwise,
     * gets it from a registered provider, or {@code null}.
     *
     * @param token   the token to get its associated object
     * @param context the context of a command execution
     * @return the object associated with the token
     */
    <T> T get(Token<T> token, ExecutionContext context);

    /**
     * Invokes the given method injecting all its parameters.
     *
     * @param cachedMethod the method to invoke
     * @param instance     an instance of the method's class to invoke it on, or {@code null} if static
     * @param context      the context of a command execution
     * @return the result of executing the method
     */
    Object invokeMethod(CachedMethod cachedMethod, Object instance, ExecutionContext context);
}
