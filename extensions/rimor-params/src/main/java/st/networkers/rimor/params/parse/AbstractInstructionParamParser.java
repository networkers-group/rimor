package st.networkers.rimor.params.parse;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.ParameterToken;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.context.provide.AbstractExecutionContextProvider;
import st.networkers.rimor.params.InstructionParam;
import st.networkers.rimor.params.InstructionParams;
import st.networkers.rimor.params.parse.support.StringInstructionParamParser;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract useful class for providers that just parse an element from the {@link InstructionParams}-annotated lists.
 * <p>
 * Check {@link StringInstructionParamParser} for a quick example.
 */
public abstract class AbstractInstructionParamParser<T> extends AbstractExecutionContextProvider<T> implements InstructionParamParser<T> {

    public static final Token<List<Object>> PARAMS_TOKEN = new Token<List<Object>>() {}.annotatedWith(InstructionParams.class);

    protected AbstractInstructionParamParser(Type providedType, Type... otherTypes) {
        super(providedType, otherTypes);
        this.annotatedWith(InstructionParam.class);
    }

    @Override
    public T get(Token<T> token, ExecutionContext context) {
        return this.parse(this.getParameter(token, context), token, context);
    }

    protected Object getParameter(Token<T> token, ExecutionContext context) {
        int index = getIndex(token, context);
        if (index < 0)
            return null;

        List<Object> commandParameters = context.get(PARAMS_TOKEN)
                .orElseThrow(() -> new IllegalArgumentException("there is no @InstructionParams annotated List<String> in the execution context!"));

        return index < commandParameters.size() ? commandParameters.get(index) : null;
    }

    protected static int getIndex(Token<?> token, ExecutionContext context) {
        InstructionParam param = token.getAnnotation(InstructionParam.class);

        // if index is given, just return it
        if (param.index() > -1)
            return param.index();

        if (token instanceof ParameterToken) {
            ParameterToken<?> parameterToken = (ParameterToken<?>) token;
            return getPositionFromParameter(parameterToken.getMethod(), parameterToken.getParameter());
        }

        return -1;
    }

    protected static int getPositionFromParameter(CachedMethod method, CachedParameter parameter) {
        // TODO cache
        List<CachedParameter> parameters = new ArrayList<>(method.getParameters());
        parameters.removeIf(p -> !p.isAnnotationPresent(InstructionParam.class)
                                 || p.getAnnotation(InstructionParam.class).index() > -1);

        return parameters.indexOf(parameter);
    }
}
