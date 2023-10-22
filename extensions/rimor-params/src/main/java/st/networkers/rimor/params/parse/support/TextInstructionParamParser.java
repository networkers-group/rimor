package st.networkers.rimor.params.parse.support;

import org.apache.commons.lang3.StringUtils;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.params.InstructionParam;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;
import st.networkers.rimor.qualify.RequireQualifiers;
import st.networkers.rimor.qualify.Token;

import java.util.List;

@RequireQualifiers({InstructionParam.class, Text.class})
public class TextInstructionParamParser extends AbstractInstructionParamParser<String> {

    public TextInstructionParamParser() {
        super(String.class);
    }

    @Override
    public String get(Token token, ExecutionContext context) {
        int index = getIndex(token, context);
        if (index < 0)
            return null;

        List<Object> commandParameters = context.<List<Object>>get(PARAMS_TOKEN)
                .orElseThrow(() -> new IllegalArgumentException("there is no @InstructionParams qualified List<String> in the execution context!"));

        return index < commandParameters.size()
                ? StringUtils.join(commandParameters.subList(index, commandParameters.size()), " ")
                : null;
    }

    @Override
    public String parse(Object parameter, Token token, ExecutionContext context) {
        // unused, overriding #get(Token, ExecutionContext)
        return null;
    }
}
