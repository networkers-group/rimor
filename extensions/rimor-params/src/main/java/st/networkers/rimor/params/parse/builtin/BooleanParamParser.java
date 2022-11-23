package st.networkers.rimor.params.parse.builtin;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.parse.AbstractParamParser;
import st.networkers.rimor.provide.RimorProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Built-in param parser for boolean types.
 * <p>
 * Registered by default. Feel free to register it again with {@link Rimor#registerProvider(RimorProvider)}
 * instantiating this class with your own aliases to parse as {@code true}, like {@code "yes"}, {@code "on"}...
 * <pre>
 * rimor.registerProvider(
 *      new BooleanParamParser("yes", "on")
 * );
 * </pre>
 */
public class BooleanParamParser extends AbstractParamParser<Boolean> {

    private final Collection<String> trueAliases;

    public BooleanParamParser(String... trueAliases) {
        this(Arrays.asList(trueAliases));
    }

    public BooleanParamParser(Collection<String> trueAliases) {
        super(boolean.class);
        this.trueAliases = trueAliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    @Override
    public Boolean parse(Object rawParameter, Token<Boolean> token, Injector injector, ExecutionContext context) {
        if (rawParameter instanceof Boolean) {
            return (Boolean) rawParameter;
        }

        if (rawParameter instanceof String) {
            String parameter = (String) rawParameter;
            return Boolean.parseBoolean(parameter) || this.trueAliases.contains(parameter.toLowerCase());
        }

        throw new IllegalArgumentException(rawParameter + " is neither a Boolean or String type");
    }
}
