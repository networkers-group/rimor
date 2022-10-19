package st.networkers.rimor.internal.provide.builtin;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.builtin.ParamParser;

/**
 * Built-in param parser for any enum type.
 */
public class EnumParamParser extends ParamParser<Enum<?>> {

    private static final TypeToken<Enum<?>> ENUM_TYPE = new TypeToken<Enum<?>>() {};

    public EnumParamParser() {
        super(ENUM_TYPE);
    }

    @Override
    protected Enum<?> parse(String parameter, Token<Enum<?>> token, Injector injector, ExecutionContext context) {
        return parse(parameter, token);
    }

    @Override
    public boolean canProvide(Token<?> token) {
        return token.getType().isSubtypeOf(ENUM_TYPE) && matchesAnnotations(token);
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
