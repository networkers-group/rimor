package st.networkers.rimor.params.parse.support;

import org.apache.commons.lang3.StringUtils;
import st.networkers.rimor.annotation.RequireQualifiers;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.params.InstructionParam;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;

import java.util.List;

@RequireQualifiers({InstructionParam.class, Text.class})
public class TextInstructionParamParser extends AbstractInstructionParamParser<String> {

    public TextInstructionParamParser() {
        super(String.class);
    }

    @Override
    public String get(Token<String> token, ExecutionContext context) {
        int index = getIndex(token, context);
        if (index < 0)
            return null;

        List<Object> commandParameters = context.get(PARAMS_TOKEN)
                .orElseThrow(() -> new IllegalArgumentException("there is no @InstructionParams annotated List<String> in the execution context!"));

        return index < commandParameters.size()
                ? StringUtils.join(commandParameters.subList(index, commandParameters.size()), " ")
                : null;
    }

    @Override
    public String parse(Object parameter, Token<String> token, ExecutionContext context) {
        // unused, overriding #get(Token, ExecutionContext)
        return null;
    }
}
