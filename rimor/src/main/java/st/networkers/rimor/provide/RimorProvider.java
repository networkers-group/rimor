package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.Annotated;

import java.util.Collection;

/**
 * Provides injectable objects that can be calculated in execution time.
 *
 * @see AbstractRimorProvider
 */
public interface RimorProvider<T> extends Annotated {

    Collection<TypeToken<T>> getProvidedTypes();

    boolean canProvide(Token<?> token, Injector injector, ExecutionContext context);

    T get(Token<T> token, Injector injector, ExecutionContext context);

}
