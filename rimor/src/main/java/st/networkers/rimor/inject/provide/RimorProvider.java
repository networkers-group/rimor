package st.networkers.rimor.inject.provide;

import st.networkers.rimor.annotation.Annotated;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Provides objects at runtime.
 *
 * @see AbstractRimorProvider
 */
public interface RimorProvider<T> extends Annotated {

    Collection<Type> getProvidedTypes();

    T get(Token<T> token, ExecutionContext context);

}
