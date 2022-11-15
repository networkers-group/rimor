package st.networkers.rimor.params.parse.builtin;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.parse.AbstractParamParser;

/**
 * Built-in basic param parser.
 */
public class PresentObjectParamParser extends AbstractParamParser<Object> {

    public PresentObjectParamParser() {
        super(Object.class);
    }

    @Override
    public Object parse(Object parameter, Token<Object> token, Injector injector, ExecutionContext context) {
        return parameter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canProvide(Token<?> token, Injector injector, ExecutionContext context) {
        if (!this.matchesAnnotations(token) || !token.matchesAnnotations(this))
            return false;

        Object object = this.getParameter((Token<Object>) token, injector, context);
        return object == null || token.getType().isSupertypeOf(object.getClass());
    }
}
