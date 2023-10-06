package st.networkers.rimor.context.provide;

import st.networkers.rimor.qualify.Qualified;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Provides context to executions.
 *
 * @see ProvidesContext
 * @see AbstractExecutionContextProvider
 */
public interface ExecutionContextProvider<T> extends Qualified {

    Collection<Type> getProvidedTypes();

    T get(Token<T> token, ExecutionContext context);

}
