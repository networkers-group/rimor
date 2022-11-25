package st.networkers.rimor.internal.provide.builtin;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.AbstractRimorProvider;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Optional;

/**
 * Provides any {@link Optional} token by delegating to a token of the type wrapped in the optional.
 */
public class OptionalProvider extends AbstractRimorProvider<Optional<?>> {

    public OptionalProvider() {
        super(new TypeToken<Optional<?>>() {});
    }

    @Override
    public Optional<?> get(Token<Optional<?>> token, Injector injector, ExecutionContext context) {
        // the type wrapped in the optional
        TypeToken<?> wrappedType = ReflectionUtils.unwrapOptional(token.getType());

        // a token associated with the type wrapped in the optional with the same annotations of the given optional token
        Token<?> wrappedToken = new Token<>(wrappedType, token.getMappedAnnotations(), token.getRequiredAnnotations());

        return injector.get(wrappedToken, context);
    }
}
