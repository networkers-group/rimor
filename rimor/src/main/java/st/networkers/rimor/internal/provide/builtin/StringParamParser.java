package st.networkers.rimor.internal.provide.builtin;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.builtin.ParamParser;

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
