package st.networkers.rimor.context.provide;

import st.networkers.rimor.annotation.Annotated;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Provides objects at runtime.
 *
 * @see AbstractExecutionContextProvider
 */
public interface ExecutionContextProvider<T> extends Annotated {

    Collection<Type> getProvidedTypes();

    T get(Token<T> token, ExecutionContext context);

}
