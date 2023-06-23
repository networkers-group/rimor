package st.networkers.rimor.params.parse.builtin;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.parse.AbstractParamParser;

public class StringParamParser extends AbstractParamParser<String> {

    public StringParamParser() {
        super(String.class);
    }

    @Override
    public String parse(Object parameter, Token<String> token, ExecutionContext context) {
        return parameter.toString();
    }

    @Override
    public boolean canProvide(Token<?> token, ExecutionContext context) {
        return this.matchesAnnotations(token) && token.matchesAnnotations(this);
    }
}
