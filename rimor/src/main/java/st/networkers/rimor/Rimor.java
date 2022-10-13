package st.networkers.rimor;

import lombok.Getter;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.CommandExecutor;
import st.networkers.rimor.internal.CommandRegistry;
import st.networkers.rimor.internal.CommandResolver;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.internal.provide.builtin.EnumParamParser;
import st.networkers.rimor.internal.provide.builtin.StringParamParser;
import st.networkers.rimor.provide.RimorProvider;

@Getter
public class Rimor {

    private final CommandRegistry registry = new CommandRegistry();

    private final Injector injector;
    private final CommandExecutor executor;

    public Rimor() {
        this.injector = new Injector()
                // built-in providers
                .registerProvider(new StringParamParser())
                .registerProvider(new EnumParamParser());

        this.executor = new CommandExecutor(injector);
    }

    public Rimor registerCommand(Command command) {
        registry.registerCommand(CommandResolver.resolve(command));
        return this;
    }

    public Rimor registerCommands(Command... commands) {
        for (Command command : commands)
            this.registerCommand(command);
        return this;
    }

    public Rimor registerProvider(RimorProvider<?> provider) {
        this.injector.registerProvider(provider);
        return this;
    }

    public Rimor registerProviders(RimorProvider<?>... providers) {
        this.injector.registerProviders(providers);
        return this;
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        return this.executor.execute(instruction, context);
    }
}
