package st.networkers.rimor;

import lombok.Getter;
import st.networkers.rimor.command.CommandDefinition;
import st.networkers.rimor.command.CommandRegistry;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.extension.ExtensionManager;
import st.networkers.rimor.extension.ExtensionManagerImpl;
import st.networkers.rimor.extension.RimorExtension;
import st.networkers.rimor.extension.event.RimorInitializationEvent;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.CommandExecutorImpl;
import st.networkers.rimor.internal.inject.InjectorImpl;
import st.networkers.rimor.internal.instruction.Instruction;
import st.networkers.rimor.internal.provide.ProviderRegistryImpl;
import st.networkers.rimor.internal.resolve.CommandResolver;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.provide.RimorProvider;

@Getter
public class Rimor {

    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final ExtensionManager extensionManager = new ExtensionManagerImpl(this);
    private final ProviderRegistry providerRegistry = new ProviderRegistryImpl();

    private final Injector injector = new InjectorImpl(providerRegistry);
    private final CommandExecutor executor = new CommandExecutorImpl(injector);
    private final boolean initialized = false;

    /**
     * Registers the given {@link CommandDefinition}.
     *
     * @param command the command to register
     */
    public Rimor registerCommand(CommandDefinition command) {
        commandRegistry.registerCommand(CommandResolver.resolve(command));
        return this;
    }

    /**
     * Registers the given {@link CommandDefinition}s.
     *
     * @param commands the commands to register
     */
    public Rimor registerCommands(CommandDefinition... commands) {
        for (CommandDefinition command : commands)
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

    /**
     * Registers the given {@link RimorExtension}.
     *
     * @param extension the extension to register
     */
    public Rimor registerExtension(RimorExtension extension) {
        this.extensionManager.registerExtension(extension);
        return this;
    }

    /**
     * Registers the given {@link RimorExtension}.
     *
     * @param extensions the extensions to register
     */
    public Rimor registerExtensions(RimorExtension... extensions) {
        for (RimorExtension extension : extensions)
            this.registerExtension(extension);
        return this;
    }

    /**
     * Initializes all the registered extensions.
     */
    public Rimor initialize() {
        this.extensionManager.callEvent(new RimorInitializationEvent(this));
        return this;
    }

    public Object execute(Instruction instruction, ExecutionContext context) {
        return this.executor.execute(instruction, context);
    }
}
