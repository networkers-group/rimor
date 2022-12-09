package st.networkers.rimor;

import lombok.Getter;
import st.networkers.rimor.command.CommandRegistry;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.CommandExecutor;
import st.networkers.rimor.execute.task.ExecutionEnclosingTaskRegistry;
import st.networkers.rimor.execute.task.ExecutionEnclosingTaskRegistryImpl;
import st.networkers.rimor.extension.ExtensionManager;
import st.networkers.rimor.extension.ExtensionManagerImpl;
import st.networkers.rimor.extension.RimorExtension;
import st.networkers.rimor.extension.event.RimorInitializationEvent;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.internal.execute.CommandExecutorImpl;
import st.networkers.rimor.internal.inject.InjectorImpl;
import st.networkers.rimor.internal.instruction.Instruction;
import st.networkers.rimor.internal.provide.ProviderRegistryImpl;
import st.networkers.rimor.internal.provide.builtin.OptionalProvider;
import st.networkers.rimor.internal.resolve.CommandResolver;
import st.networkers.rimor.internal.resolve.PathResolverImpl;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.provide.RimorProvider;
import st.networkers.rimor.resolve.PathResolver;

@Getter
public class Rimor {

    private final CommandRegistry commandRegistry;
    private final ProviderRegistry providerRegistry;
    private final ExecutionEnclosingTaskRegistry executionEnclosingTaskRegistry;
    private final ExtensionManager extensionManager;

    private final Injector injector;
    private final PathResolver pathResolver;
    private final CommandExecutor executor;

    private final boolean initialized = false;

    public Rimor() {
        this(new CommandRegistry(), new ProviderRegistryImpl(), new ExecutionEnclosingTaskRegistryImpl(), new ExtensionManagerImpl());
    }

    public Rimor(CommandRegistry commandRegistry, ProviderRegistry providerRegistry,
                 ExecutionEnclosingTaskRegistry executionEnclosingTaskRegistry,
                 ExtensionManager extensionManager) {
        this(commandRegistry, providerRegistry, executionEnclosingTaskRegistry, extensionManager, new InjectorImpl(providerRegistry));
    }

    public Rimor(CommandRegistry commandRegistry, ProviderRegistry providerRegistry,
                 ExecutionEnclosingTaskRegistry executionEnclosingTaskRegistry,
                 ExtensionManager extensionManager, Injector injector) {
        this(commandRegistry, providerRegistry, executionEnclosingTaskRegistry, extensionManager, injector,
                new PathResolverImpl(executionEnclosingTaskRegistry, injector), new CommandExecutorImpl(injector));
    }

    public Rimor(CommandRegistry commandRegistry, ProviderRegistry providerRegistry,
                 ExecutionEnclosingTaskRegistry executionEnclosingTaskRegistry, ExtensionManager extensionManager,
                 Injector injector, PathResolver pathResolver, CommandExecutor executor) {
        this.commandRegistry = commandRegistry;
        this.providerRegistry = providerRegistry;
        this.executionEnclosingTaskRegistry = executionEnclosingTaskRegistry;
        this.extensionManager = extensionManager;
        this.injector = injector;
        this.pathResolver = pathResolver;
        this.executor = executor;

        this.registerProvider(new OptionalProvider());
    }

    /**
     * Registers the given {@link RimorCommand}.
     *
     * @param command the command to register
     */
    public Rimor registerCommand(RimorCommand command) {
        commandRegistry.registerCommand(CommandResolver.resolve(command));
        return this;
    }

    /**
     * Registers the given {@link RimorCommand}s.
     *
     * @param commands the commands to register
     */
    public Rimor registerCommands(RimorCommand... commands) {
        for (RimorCommand command : commands)
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
        this.extensionManager.registerExtension(this, extension);
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
