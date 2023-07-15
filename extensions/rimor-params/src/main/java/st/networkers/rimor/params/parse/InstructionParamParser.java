package st.networkers.rimor.params.parse;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.params.InstructionParams;
import st.networkers.rimor.params.parse.support.BooleanInstructionParamParser;
import st.networkers.rimor.params.parse.support.DefaultInstructionParamParser;
import st.networkers.rimor.params.parse.support.EnumInstructionParamParser;
import st.networkers.rimor.params.parse.support.StringInstructionParamParser;
import st.networkers.rimor.context.provide.ExecutionContextProvider;

/**
 * Abstract class for providers that just parse an element from the {@link InstructionParams}-annotated lists.
 *
 * @see AbstractInstructionParamParser
 * @see BooleanInstructionParamParser
 * @see DefaultInstructionParamParser
 * @see EnumInstructionParamParser
 * @see StringInstructionParamParser
 */
public interface InstructionParamParser<T> extends ExecutionContextProvider<T> {

    T parse(Object parameter, Token<T> token, ExecutionContext context);

}
