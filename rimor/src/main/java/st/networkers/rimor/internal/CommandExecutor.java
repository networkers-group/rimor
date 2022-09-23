package st.networkers.rimor.internal;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.internal.provide.ParameterProviderRegistry;
import st.networkers.rimor.util.InjectionUtils;

public class CommandExecutor {

    private final ParameterProviderRegistry parameterProviderRegistry;

    public CommandExecutor(ParameterProviderRegistry parameterProviderRegistry) {
        this.parameterProviderRegistry = parameterProviderRegistry;
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        return InjectionUtils.invokeMethod(
                instruction.getMethod(),
                instruction.getCommandInstance(),
                context,
                this.parameterProviderRegistry
        );
    }
}
