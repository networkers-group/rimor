package st.networkers.rimor;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.CommandExecutor;
import st.networkers.rimor.internal.CommandRegistry;
import st.networkers.rimor.internal.CommandResolver;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.internal.provide.builtin.ParamProvider;
import st.networkers.rimor.internal.provide.ParameterProviderRegistry;

public class Rimor {

    private final CommandRegistry registry = new CommandRegistry();
    private final ParameterProviderRegistry parameterProviderRegistry;
    private final CommandExecutor executor;

    public Rimor() {
        this.parameterProviderRegistry = new ParameterProviderRegistry();
        this.executor = new CommandExecutor(parameterProviderRegistry);

        this.registerParameterProviders(new ParamProvider());
    }

    public void registerCommand(Command command) {
        registry.registerCommand(CommandResolver.resolve(command));
    }

    public void registerParameterProviders(Object... instances) {
        this.parameterProviderRegistry.register(instances);
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        return this.executor.execute(instruction, context);
    }
}
