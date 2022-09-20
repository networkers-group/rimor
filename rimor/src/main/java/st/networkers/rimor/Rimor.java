package st.networkers.rimor;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.CommandExecutor;
import st.networkers.rimor.internal.CommandRegistry;
import st.networkers.rimor.internal.CommandResolver;
import st.networkers.rimor.internal.command.CommandInstruction;
import st.networkers.rimor.internal.builtin.ParamProvider;
import st.networkers.rimor.internal.provide.ParameterProviderRegistry;

public class Rimor {

    private final CommandRegistry registry = new CommandRegistry();
    private final CommandExecutor executor;

    public Rimor() {
        ParameterProviderRegistry parameterProviderRegistry = new ParameterProviderRegistry();
        parameterProviderRegistry.register(new ParamProvider());

        this.executor = new CommandExecutor(parameterProviderRegistry);
    }

    public void registerCommand(Command command) {
        registry.registerCommand(CommandResolver.resolve(command));
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        return this.executor.execute(instruction, context);
    }
}
