package st.networkers.rimor.params.parse;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.inject.ParameterToken;
import st.networkers.rimor.params.InstructionParam;
import st.networkers.rimor.params.InstructionParams;
import st.networkers.rimor.params.parse.support.StringInstructionParamParser;
import st.networkers.rimor.inject.provide.AbstractRimorProvider;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract useful class for implementing {@link InstructionParamParser}s.
 * <p>
 * Check {@link StringInstructionParamParser} for a quick example.
 */
public abstract class AbstractInstructionParamParser<T> extends AbstractRimorProvider<T> implements InstructionParamParser<T> {

    protected static final Token<List<Object>> PARAMS_TOKEN = new Token<List<Object>>() {}.annotatedWith(InstructionParams.class);

    @SafeVarargs
    protected AbstractInstructionParamParser(Class<? extends T> providedType, Class<? extends T>... otherTypes) {
        super(providedType, otherTypes);
        this.annotatedWith(InstructionParam.class);
    }

    protected AbstractInstructionParamParser(Type providedType, Type... otherTypes) {
        super(providedType, otherTypes);
        this.annotatedWith(InstructionParam.class);
    }

    @Override
    public T get(Token<T> token, ExecutionContext context) {
        return this.parse(this.getParameter(token, context), token, context);
    }

    private Object getParameter(Token<T> token, ExecutionContext context) {
        int position = this.getPosition(token, context);
        List<Object> commandParameters = context.get(PARAMS_TOKEN)
                .orElseThrow(() -> new IllegalArgumentException("there is no @InstructionParams annotated List<String> in the execution context!"));

        return position >= 0
                ? position < commandParameters.size() ? commandParameters.get(position) : null
                : null;
    }

    private int getPosition(Token<T> token, ExecutionContext context) {
        InstructionParam param = token.getAnnotation(InstructionParam.class);

        // if position is indicated, just return it
        if (param.index() > -1)
            return param.index();

        if (token instanceof ParameterToken) {
            ParameterToken<T> parameterToken = (ParameterToken<T>) token;
            return this.getPositionFromParameter(parameterToken.getMethod(), parameterToken.getParameter());
        }

        return -1;
    }

    private int getPositionFromParameter(CachedMethod method, CachedParameter parameter) {
        // TODO cache
        List<CachedParameter> parameters = new ArrayList<>(method.getParameters());
        parameters.removeIf(p -> !p.isAnnotationPresent(InstructionParam.class)
                                 || p.getAnnotation(InstructionParam.class).index() > -1);

        return parameters.indexOf(parameter);
    }
}
