package st.networkers.rimor.params.parse.support;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;

public class StringInstructionParamParser extends AbstractInstructionParamParser<String> {

    public StringInstructionParamParser() {
        super(String.class);
    }

    @Override
    public String parse(Object parameter, Token token, ExecutionContext context) {
        return parameter.toString();
    }
}
