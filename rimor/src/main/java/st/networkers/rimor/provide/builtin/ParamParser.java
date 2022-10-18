package st.networkers.rimor.provide.builtin;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.RimorProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for providers that just parse an element from the {@link Params}-annotated string lists.
 *
 * @see st.networkers.rimor.internal.provide.builtin.BooleanParamParser
 * @see st.networkers.rimor.internal.provide.builtin.EnumParamParser
 * @see st.networkers.rimor.internal.provide.builtin.StringParamParser
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

    @Override
    public T get(Token<T> token, Injector injector, ExecutionContext context) {
        int position = token.getAnnotation(Param.class).value();
        List<String> commandParameters = context.get(PARAMS_TOKEN).orElseGet(ArrayList::new);

        return position < commandParameters.size()
                ? this.parse(commandParameters.get(position), token, injector, context)
                : null;
    }

    protected abstract T parse(String parameter, Token<T> token, Injector injector, ExecutionContext context);
}
