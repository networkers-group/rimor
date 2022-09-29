package st.networkers.rimor;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.CommandExecutor;
import st.networkers.rimor.internal.CommandRegistry;
import st.networkers.rimor.internal.CommandResolver;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.internal.provide.builtin.ExecutionParametersProvider;
import st.networkers.rimor.internal.provide.ParameterProviderRegistry;
import st.networkers.rimor.provide.ParameterProviderWrapper;

public class Rimor {

    private final CommandRegistry registry = new CommandRegistry();
    private final ParameterProviderRegistry parameterProviderRegistry;
    private final CommandExecutor executor;

    public Rimor() {
        this.parameterProviderRegistry = new ParameterProviderRegistry();
        this.executor = new CommandExecutor(parameterProviderRegistry);

        this.registerParameterProviders(new ExecutionParametersProvider());
    }

    public void registerCommand(Command command) {
        registry.registerCommand(CommandResolver.resolve(command));
    }

    public void registerParameterProviders(ParameterProviderWrapper... wrappers) {
        this.parameterProviderRegistry.register(wrappers);
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        return this.executor.execute(instruction, context);
    }
}
