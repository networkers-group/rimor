package st.networkers.rimor.params.parse.builtin;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.parse.ParamParser;

/**
 * Built-in basic param parser.
 */
public class StringParamParser extends ParamParser<String> {

    public StringParamParser() {
        super(String.class);
    }

    @Override
    protected String parse(String parameter, Token<String> token, Injector injector, ExecutionContext context) {
        return parameter;
    }
}
