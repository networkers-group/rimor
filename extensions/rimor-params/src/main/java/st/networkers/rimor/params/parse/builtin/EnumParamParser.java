package st.networkers.rimor.params.parse.builtin;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.parse.AbstractParamParser;

/**
 * Built-in param parser for any enum type.
 */
public class EnumParamParser extends AbstractParamParser<Enum<?>> {

    private static final TypeToken<Enum<?>> ENUM_TYPE = new TypeToken<Enum<?>>() {};

    public EnumParamParser() {
        super(ENUM_TYPE);
    }

    @Override
    public Enum<?> parse(Object rawParameter, Token<Enum<?>> token, ExecutionContext context) {
        if (rawParameter instanceof Enum<?>) {
            return (Enum<?>) rawParameter;
        }

        if (rawParameter instanceof String) {
            String parameter = (String) rawParameter;
            return parse(parameter.toUpperCase(), token);
        }

        throw new IllegalArgumentException(rawParameter + " is neither an Enum or String type");
    }

    @Override
    public boolean canProvide(Token<?> token, ExecutionContext context) {
        return token.getType().isSubtypeOf(ENUM_TYPE)
               && this.matchesAnnotations(token)
               && token.matchesAnnotations(this);
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> T parse(String parameter, Token<? extends Enum<?>> token) {
        try {
            return Enum.valueOf((Class<T>) token.getType().getRawType(), parameter);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
