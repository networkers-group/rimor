package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.annotated.Annotated;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;

import java.util.Collection;

/**
 * Provides injectable objects at runtime.
 *
 * @see AbstractRimorProvider
 */
public interface RimorProvider<T> extends Annotated {

    Collection<TypeToken<? extends T>> getProvidedTypes();

    boolean canProvide(Token<?> token, ExecutionContext context);

    T get(Token<T> token, ExecutionContext context);

}
