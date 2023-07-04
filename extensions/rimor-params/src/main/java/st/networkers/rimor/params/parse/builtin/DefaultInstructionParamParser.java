package st.networkers.rimor.params.parse.builtin;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;

/**
 * Built-in basic param parser.
 */
public class DefaultInstructionParamParser extends AbstractInstructionParamParser<Object> {

    public DefaultInstructionParamParser() {
        super(Object.class);
    }

    @Override
    public Object parse(Object parameter, Token<Object> token, ExecutionContext context) {
        return parameter;
    }
}
