package st.networkers.rimor.internal.provide.builtin;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.RimorProvider;
import st.networkers.rimor.provide.builtin.ParamParser;

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
public class BooleanParamParser extends ParamParser<Boolean> {

    private final Collection<String> trueAliases;

    public BooleanParamParser(String... trueAliases) {
        this(Arrays.asList(trueAliases));
    }

    public BooleanParamParser(Collection<String> trueAliases) {
        super(boolean.class, Boolean.class);
        this.trueAliases = trueAliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    @Override
    protected Boolean parse(String parameter, Token<Boolean> token, Injector injector, ExecutionContext context) {
        return Boolean.parseBoolean(parameter) || trueAliases.contains(parameter.toLowerCase());
    }
}
