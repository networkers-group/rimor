package st.networkers.rimor.context;

import st.networkers.rimor.context.provide.ExecutionContextProvider;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;

import java.util.Optional;

/**
 * Provides an object bound to a given {@link Token} from an {@link ExecutionContext} or a registered provider.
 */
public interface ExecutionContextService {

    /**
     * Gets an {@link Optional} wrapping the object bound to the given {@link Token} from the given
     * {@link ExecutionContext}, if able. Otherwise, gets it from a registered {@link ExecutionContextProvider}, or
     * returns an empty {@code Optional} if there are no available {@code ExecutionContextProvider} for the
     * given {@code Token}.
     *
     * @param token   the token to get its bound object
     * @param context the context of the command execution
     * @return an {@link Optional} wrapping the object bound to the token, or empty
     */
    <T> Optional<T> get(Token<T> token, ExecutionContext context);

    /**
     * Gets an {@link Optional} wrapping the object bound to the given {@link Token} from the given
     * {@link ExecutionContext}, if able. Otherwise, gets it from a registered {@link ExecutionContextProvider}, or
     * returns an empty {@code Optional} if there are no available {@code ExecutionContextProvider} for the
     * given {@code Token}.
     *
     * @param token   the token to get its bound object
     * @param bean    the bean to use its local providers
     * @param context the context of the command execution
     * @return an {@link Optional} wrapping the object bound to the token, or empty
     */
    <T> Optional<T> get(Token<T> token, Object bean, ExecutionContext context);

    /**
     * Registers the given {@link ExecutionContextProvider} globally, for all Rimor beans.
     */
    void registerGlobalExecutionContextProvider(ExecutionContextProvider<?> executionContextProvider);

    /**
     * Registers the given {@link ExecutionContextProvider} locally for the given bean.
     */
    void registerExecutionContextProvider(ExecutionContextProvider<?> executionContextProvider, Object bean);

    /**
     * Invokes the given method injecting all its parameters following {@link #get(Token, ExecutionContext)}
     *
     * @param qualifiedMethod the method to invoke
     * @param instance        an instance of the method's class to invoke it on, or {@code null} if static
     * @param context         the context of the command execution
     * @return the result of executing the method
     */
    Object invokeMethod(QualifiedMethod qualifiedMethod, Object instance, ExecutionContext context);

}
