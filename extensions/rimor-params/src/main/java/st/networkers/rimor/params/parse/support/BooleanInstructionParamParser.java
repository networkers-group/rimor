package st.networkers.rimor.params.parse.support;

import org.apache.commons.lang3.ArrayUtils;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Built-in param parser for boolean types.
 */
public class BooleanInstructionParamParser extends AbstractInstructionParamParser<Boolean> {

    private final Collection<String> trueAliases;

    public BooleanInstructionParamParser(String... trueAliases) {
        this(Arrays.asList(trueAliases));
    }

    public BooleanInstructionParamParser(Collection<String> trueAliases) {
        super(boolean.class);
        this.trueAliases = trueAliases.stream().map(String::toLowerCase).collect(Collectors.toSet());
    }

    @Override
    public Boolean parse(Object param, Token token, ExecutionContext context) {
        if (param instanceof Boolean) {
            return (Boolean) param;
        }

        if (param instanceof String) {
            return this.parse((String) param, token);
        }

        throw new IllegalArgumentException(param + " is neither a Boolean or String type");
    }

    private boolean parse(String parameter, Token token) {
        if (Boolean.parseBoolean(parameter) || this.trueAliases.contains(parameter.toLowerCase()))
            return true;

        TrueValues trueValues = token.getQualifier(TrueValues.class);
        return trueValues != null && ArrayUtils.contains(trueValues.value(), parameter);
    }
}
