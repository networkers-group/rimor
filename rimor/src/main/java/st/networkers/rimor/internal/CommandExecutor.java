package st.networkers.rimor.internal;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.internal.provide.ParameterProviderRegistry;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.util.InjectionUtils;
import st.networkers.rimor.util.ReflectionUtils;

public class CommandExecutor {

    private final ParameterProviderRegistry parameterProviderRegistry;

    public CommandExecutor(ParameterProviderRegistry parameterProviderRegistry) {
        this.parameterProviderRegistry = parameterProviderRegistry;
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        CachedMethod cachedMethod = instruction.getMethod();

        Object[] parameters = InjectionUtils.resolve(
                cachedMethod,
                context,
                parameter -> this.fromProvider(parameter, context)
        );

        return ReflectionUtils.invoke(cachedMethod.getMethod(), instruction.getCommandInstance(), parameters);
    }

    private Object fromProvider(CachedParameter parameter, ExecutionContext context) {
        return parameterProviderRegistry.findFor(parameter)
                .map(provider -> provider.get(parameter, context))
                .orElse(null);
    }
}
