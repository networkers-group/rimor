package st.networkers.rimor.context.provide.support;

import org.apache.commons.lang3.reflect.TypeLiteral;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.context.provide.AbstractExecutionContextProvider;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Provides any {@link Optional} token by delegating to a token of the type wrapped in the optional.
 */
public class OptionalProvider extends AbstractExecutionContextProvider<Optional<Object>> {

    private final ExecutionContextService executionContextService;

    public OptionalProvider(ExecutionContextService executionContextService) {
        super(new TypeLiteral<Optional<?>>() {}.getType());
        this.executionContextService = executionContextService;
    }

    @Override
    public Optional<Object> get(Token token, ExecutionContext context) {
        // the type wrapped in the optional
        Type wrappedType = ReflectionUtils.unwrapOptional(token.getType());

        // a token associated with the type and the same annotations of the optional
        Token wrappedToken = Token.of(wrappedType, token.getQualifiersMap(), token.getRequiredQualifiers());

        return executionContextService.get(wrappedToken, context);
    }
}
