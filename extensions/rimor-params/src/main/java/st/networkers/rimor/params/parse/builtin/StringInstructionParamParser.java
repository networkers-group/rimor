package st.networkers.rimor.params.parse.builtin;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;

public class StringInstructionParamParser extends AbstractInstructionParamParser<String> {

    public StringInstructionParamParser() {
        super(String.class);
    }

    @Override
    public String parse(Object parameter, Token<String> token, ExecutionContext context) {
        return parameter.toString();
    }
}
