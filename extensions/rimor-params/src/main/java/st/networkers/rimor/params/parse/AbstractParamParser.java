package st.networkers.rimor.params.parse;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.instruction.Instruction;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.params.Param;
import st.networkers.rimor.params.Params;
import st.networkers.rimor.provide.AbstractRimorProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParamParser<T> extends AbstractRimorProvider<T> implements ParamParser<T> {

    protected static final Token<List<Object>> PARAMS_TOKEN = new Token<>(new TypeToken<List<Object>>() {}).annotatedWith(Params.class);
    private static final Token<Instruction> INSTRUCTION_TOKEN = new Token<>(Instruction.class);
    private static final Token<CachedParameter> PARAMETER_TOKEN = new Token<>(CachedParameter.class);

    @SafeVarargs
    protected AbstractParamParser(Class<T>... providedTypes) {
        super(providedTypes);
        annotatedWith(Param.class);
    }

    @SafeVarargs
    protected AbstractParamParser(TypeToken<T>... providedTypes) {
        super(providedTypes);
        annotatedWith(Param.class);
    }

    @Override
    public T get(Token<T> token, Injector injector, ExecutionContext context) {
        return this.parse(this.getParameter(token, injector, context), token, injector, context);
    }

    @Override
    public Object getParameter(Token<T> token, Injector injector, ExecutionContext context) {
        int position = this.getPosition(token, context);
        List<Object> commandParameters = context.get(PARAMS_TOKEN).orElseGet(ArrayList::new);

        return position < commandParameters.size() ? commandParameters.get(position) : null;
    }

    private int getPosition(Token<T> token, ExecutionContext context) {
        Param param = token.getAnnotation(Param.class);

        // if position is indicated, just return it
        if (param.position() > -1)
            return param.position();

        // if not, return the position of the current parameter
        Instruction instruction = context.get(INSTRUCTION_TOKEN).orElseThrow(IllegalArgumentException::new);
        CachedParameter parameter = context.get(PARAMETER_TOKEN).orElseThrow(IllegalArgumentException::new);

        return this.getPositionFromParameter(instruction, parameter);
    }

    private int getPositionFromParameter(Instruction instruction, CachedParameter parameter) {
        List<CachedParameter> parameters = new ArrayList<>(instruction.getMethod().getParameters());
        parameters.removeIf(p -> !p.isAnnotationPresent(Param.class));

        return parameters.indexOf(parameter);
    }
}
