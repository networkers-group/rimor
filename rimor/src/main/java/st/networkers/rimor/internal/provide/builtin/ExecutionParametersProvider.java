package st.networkers.rimor.internal.provide.builtin;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.provide.RimorProvider;
import st.networkers.rimor.provide.builtin.Param;
import st.networkers.rimor.provide.builtin.Params;

import java.util.ArrayList;
import java.util.List;

public class ExecutionParametersProvider extends RimorProvider<String> {

    public ExecutionParametersProvider() {
        super(String.class);
    }

    @RequireAnnotations(Param.class)
    @Override
    public String get(Token<? super String> token, Injector injector, ExecutionContext context) {
        List<String> commandParameters = this.getParameters(context);
        Param param = token.getAnnotation(Param.class);

        int position = param.value();
        return position < commandParameters.size() ? commandParameters.get(position) : null;
    }

    private List<String> getParameters(ExecutionContext context) {
        return context.get(
                new Token<>(new TypeToken<List<String>>() {}).annotatedWith(Params.class)
        ).orElseGet(ArrayList::new);
    }
}
