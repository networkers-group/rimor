package st.networkers.rimor.params.parse;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.instruction.ResolvedInstruction;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.params.Param;
import st.networkers.rimor.params.Params;
import st.networkers.rimor.params.parse.builtin.BooleanParamParser;
import st.networkers.rimor.params.parse.builtin.EnumParamParser;
import st.networkers.rimor.params.parse.builtin.StringParamParser;
import st.networkers.rimor.provide.RimorProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for providers that just parse an element from the {@link Params}-annotated string lists.
 *
 * @see BooleanParamParser
 * @see EnumParamParser
 * @see StringParamParser
 */
public abstract class ParamParser<T> extends RimorProvider<T> {

    private static final Token<List<String>> PARAMS_TOKEN = new Token<>(new TypeToken<List<String>>() {}).annotatedWith(Params.class);

    @SafeVarargs
    protected ParamParser(Class<T>... providedTypes) {
        super(providedTypes);
        annotatedWith(Param.class);
    }

    @SafeVarargs
    protected ParamParser(TypeToken<T>... providedTypes) {
        super(providedTypes);
        annotatedWith(Param.class);
    }

    protected abstract T parse(String parameter, Token<T> token, Injector injector, ExecutionContext context);

    @Override
    public T get(Token<T> token, Injector injector, ExecutionContext context) {
        int position = this.getPosition(token, context);
        List<String> commandParameters = context.get(PARAMS_TOKEN).orElseGet(ArrayList::new);

        return position < commandParameters.size()
                ? this.parse(commandParameters.get(position), token, injector, context)
                : null;
    }

    private static final Token<ResolvedInstruction> INSTRUCTION_TOKEN = new Token<>(ResolvedInstruction.class);
    private static final Token<CachedParameter> PARAMETER_TOKEN = new Token<>(CachedParameter.class);

    private int getPosition(Token<T> token, ExecutionContext context) {
        Param param = token.getAnnotation(Param.class);

        // if position is indicated, just return it
        if (param.position() > -1)
            return param.position();

        // if not, return the position of the current parameter
        ResolvedInstruction instruction = context.get(INSTRUCTION_TOKEN).orElseThrow(IllegalArgumentException::new);
        CachedParameter parameter = context.get(PARAMETER_TOKEN).orElseThrow(IllegalArgumentException::new);

        return this.getPositionFromParameter(instruction, parameter);
    }

    private int getPositionFromParameter(ResolvedInstruction instruction, CachedParameter parameter) {
        List<CachedParameter> parameters = new ArrayList<>(instruction.getMethod().getParameters());
        parameters.removeIf(p -> !p.isAnnotationPresent(Param.class));

        int position = parameters.indexOf(parameter);
        if (position < 0)
            throw new IllegalArgumentException();

        return position;
    }
}
