package st.networkers.rimor.inject.provide.support;

import org.apache.commons.lang3.reflect.TypeLiteral;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.inject.provide.AbstractRimorProvider;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Provides any {@link Optional} token by delegating to a token of the type wrapped in the optional.
 */
public class OptionalProvider extends AbstractRimorProvider<Optional<?>> {

    private final RimorInjector injector;

    public OptionalProvider(RimorInjector injector) {
        super(new TypeLiteral<Optional<?>>() {}.getType());
        this.injector = injector;
    }

    @Override
    public Optional<?> get(Token<Optional<?>> token, ExecutionContext context) {
        // the type wrapped in the optional
        Type wrappedType = ReflectionUtils.unwrapOptional(token.getType());

        // a token associated with the type wrapped in the optional with the same annotations of the given optional token
        Token<?> wrappedToken = Token.of(wrappedType, token.getAnnotatedProperties());

        return injector.get(wrappedToken, context);
    }
}
