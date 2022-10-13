package st.networkers.rimor.internal.provide.builtin;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
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
        try {
            return parse(parameter, token);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public boolean canProvide(Token<?> token) {
        return token.getType().isSubtypeOf(ENUM_TYPE) && matchesAnnotations(token);
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> T parse(String parameter, Token<? extends Enum<?>> token) {
        return Enum.valueOf((Class<T>) token.getType().getRawType(), parameter);
    }
}
