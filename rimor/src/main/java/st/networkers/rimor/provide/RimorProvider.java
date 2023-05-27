package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Annotated;
import st.networkers.rimor.inject.Token;

import java.util.Collection;

/**
 * Provides injectable objects that are generated in execution time.
 *
 * @see AbstractRimorProvider
 */
public interface RimorProvider<T> extends Annotated {

    Collection<TypeToken<? extends T>> getProvidedTypes();

    boolean canProvide(Token<?> token, ExecutionContext context);

    T get(Token<T> token, ExecutionContext context);

}
