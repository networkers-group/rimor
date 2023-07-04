package st.networkers.rimor.params.parse;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.InstructionParams;
import st.networkers.rimor.params.parse.builtin.BooleanInstructionParamParser;
import st.networkers.rimor.params.parse.builtin.DefaultInstructionParamParser;
import st.networkers.rimor.params.parse.builtin.EnumInstructionParamParser;
import st.networkers.rimor.params.parse.builtin.StringInstructionParamParser;
import st.networkers.rimor.provide.RimorProvider;

/**
 * Abstract class for providers that just parse an element from the {@link InstructionParams}-annotated lists.
 *
 * @see AbstractInstructionParamParser
 * @see BooleanInstructionParamParser
 * @see DefaultInstructionParamParser
 * @see EnumInstructionParamParser
 * @see StringInstructionParamParser
 */
public interface InstructionParamParser<T> extends RimorProvider<T> {

    T parse(Object parameter, Token<T> token, ExecutionContext context);

}
