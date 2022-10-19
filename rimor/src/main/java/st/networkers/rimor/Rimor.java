package st.networkers.rimor;

import lombok.Getter;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.CommandExecutor;
import st.networkers.rimor.internal.CommandExecutorImpl;
import st.networkers.rimor.internal.CommandRegistry;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.InjectorImpl;
import st.networkers.rimor.internal.instruction.CommandInstruction;
import st.networkers.rimor.internal.provide.ProviderRegistry;
import st.networkers.rimor.internal.provide.ProviderRegistryImpl;
import st.networkers.rimor.internal.resolve.CommandResolver;
import st.networkers.rimor.provide.RimorProvider;

@Getter
public class Rimor {

    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final ProviderRegistry providerRegistry = new ProviderRegistryImpl();

    private final Injector injector = new InjectorImpl(providerRegistry);
    private final CommandExecutor executor = new CommandExecutorImpl(injector);

    /**
     * Registers the given {@link Command}.
     *
     * @param command the command to register
     */
    public Rimor registerCommand(Command command) {
        commandRegistry.registerCommand(CommandResolver.resolve(command));
        return this;
    }

    /**
     * Registers the given {@link Command}s.
     *
     * @param commands the commands to register
     */
    public Rimor registerCommands(Command... commands) {
        for (Command command : commands)
            this.registerCommand(command);
        return this;
    }

    /**
     * Registers the given {@link RimorProvider}.
     *
     * @param provider the provider to register into this injector
     */
    public Rimor registerProvider(RimorProvider<?> provider) {
        this.providerRegistry.register(provider);
        return this;
    }

    /**
     * Registers the given {@link RimorProvider}s.
     *
     * @param providers the providers to register into this injector
     */
    public Rimor registerProviders(RimorProvider<?>... providers) {
        for (RimorProvider<?> provider : providers)
            this.registerProvider(provider);
        return this;
    }

    public Object execute(CommandInstruction instruction, ExecutionContext context) {
        return this.executor.execute(instruction, context);
    }
}
