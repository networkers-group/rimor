package st.networkers.rimor.params.parse.support;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;

/**
 * Built-in param parser for any enum type.
 */
public class EnumInstructionParamParser extends AbstractInstructionParamParser<Enum<?>> {

    public EnumInstructionParamParser() {
        super(Enum.class);
    }

    @Override
    public Enum<?> parse(Object rawParameter, Token token, ExecutionContext context) {
        if (rawParameter instanceof Enum<?>) {
            return (Enum<?>) rawParameter;
        }

        if (rawParameter instanceof String) {
            String parameter = (String) rawParameter;
            return parse(parameter.toUpperCase(), token);
        }

        throw new IllegalArgumentException(rawParameter + " is neither an Enum or String type");
    }

    @SuppressWarnings("unchecked")
    private <T extends Enum<T>> T parse(String parameter, Token token) {
        try {
            return Enum.valueOf((Class<T>) token.getType(), parameter);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
