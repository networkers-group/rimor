package st.networkers.rimor.params.parse;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.Params;
import st.networkers.rimor.provide.RimorProvider;

/**
 * Abstract class for providers that just parse an element from the {@link Params}-annotated lists.
 *
 * @see AbstractParamParser
 * @see st.networkers.rimor.params.parse.builtin.BooleanParamParser
 * @see st.networkers.rimor.params.parse.builtin.DefaultParamParser
 * @see st.networkers.rimor.params.parse.builtin.EnumParamParser
 * @see st.networkers.rimor.params.parse.builtin.StringParamParser
 */
public interface ParamParser<T> extends RimorProvider<T> {

    Object getParameter(Token<T> token, ExecutionContext context);

    T parse(Object parameter, Token<T> token, ExecutionContext context);

}
