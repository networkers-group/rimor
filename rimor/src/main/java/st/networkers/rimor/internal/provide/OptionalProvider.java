package st.networkers.rimor.internal.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.AbstractRimorProvider;
import st.networkers.rimor.util.ReflectionUtils;

import java.util.Optional;

/**
 * Provides any {@link Optional} token by delegating to a token of the type wrapped in the optional.
 */
public class OptionalProvider extends AbstractRimorProvider<Optional<?>> {

    private final RimorInjector injector;

    public OptionalProvider(RimorInjector injector) {
        super(new TypeToken<Optional<?>>() {});
        this.injector = injector;
    }

    @Override
    public Optional<?> get(Token<Optional<?>> token, ExecutionContext context) {
        // the type wrapped in the optional
        TypeToken<?> wrappedType = ReflectionUtils.unwrapOptional(token.getType());

        // a token associated with the type wrapped in the optional with the same annotations of the given optional token
        Token<?> wrappedToken = new Token<>(wrappedType, token.getAnnotatedProperties());

        return injector.get(wrappedToken, context);
    }
}
