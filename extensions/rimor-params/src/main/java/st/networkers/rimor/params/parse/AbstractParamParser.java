package st.networkers.rimor.params.parse;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.ParameterToken;
import st.networkers.rimor.params.Param;
import st.networkers.rimor.params.Params;
import st.networkers.rimor.provide.AbstractRimorProvider;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract useful class for implementing {@link ParamParser}s.
 * <p>
 * Check {@link st.networkers.rimor.params.parse.builtin.StringParamParser} for a quick example.
 */
public abstract class AbstractParamParser<T> extends AbstractRimorProvider<T> implements ParamParser<T> {

    protected static final Token<List<Object>> PARAMS_TOKEN = new Token<>(new TypeToken<List<Object>>() {}).annotatedWith(Params.class);

    @SafeVarargs
    protected AbstractParamParser(Class<? extends T>... providedTypes) {
        super(providedTypes);
        annotatedWith(Param.class);
    }

    @SafeVarargs
    protected AbstractParamParser(TypeToken<? extends T>... providedTypes) {
        super(providedTypes);
        annotatedWith(Param.class);
    }

    @Override
    public T get(Token<T> token, ExecutionContext context) {
        return this.parse(this.getParameter(token, context), token, context);
    }

    @Override
    public Object getParameter(Token<T> token, ExecutionContext context) {
        int position = this.getPosition(token, context);
        List<Object> commandParameters = context.get(PARAMS_TOKEN).orElseGet(ArrayList::new);

        return position >= 0
                ? position < commandParameters.size() ? commandParameters.get(position) : null
                : null;
    }

    private int getPosition(Token<T> token, ExecutionContext context) {
        Param param = token.getAnnotation(Param.class);

        // if position is indicated, just return it
        if (param.position() > -1)
            return param.position();

        if (token instanceof ParameterToken) {
            ParameterToken<T> parameterToken = (ParameterToken<T>) token;
            return this.getPositionFromParameter(parameterToken.getMethod(), parameterToken.getParameter());
        }

        return -1;
    }

    private int getPositionFromParameter(CachedMethod method, CachedParameter parameter) {
        List<CachedParameter> parameters = new ArrayList<>(method.getParameters());
        parameters.removeIf(p -> !p.isAnnotationPresent(Param.class));

        return parameters.indexOf(parameter);
    }
}
