package st.networkers.rimor.provide.builtin;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.internal.provide.builtin.EnumParamParser;
import st.networkers.rimor.internal.provide.builtin.StringParamParser;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.provide.RimorProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @see EnumParamParser
 * @see StringParamParser
 */
public abstract class ParamParser<T> extends RimorProvider<T> {

    protected ParamParser(Class<T> providedType) {
        super(providedType);
    }

    protected ParamParser(TypeToken<T> providedType) {
        super(providedType);
    }

    protected abstract T parse(String parameter, Token<T> token, Injector injector, ExecutionContext context);

    @RequireAnnotations(Param.class)
    @Override
    public T get(Token<T> token, Injector injector, ExecutionContext context) {
        List<String> commandParameters = this.getParameters(context);
        int position = token.getAnnotation(Param.class).value();

        return position < commandParameters.size()
                ? this.parse(commandParameters.get(position), token, injector, context)
                : null;
    }

    private List<String> getParameters(ExecutionContext context) {
        Token<List<String>> token = new Token<>(new TypeToken<List<String>>() {})
                .annotatedWith(Params.class);

        return context.get(token).orElseGet(ArrayList::new);
    }
}
