package st.networkers.rimor;

import st.networkers.rimor.command.CommandRegistry;
import st.networkers.rimor.command.CommandResolver;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.execute.CommandExecutor;
import st.networkers.rimor.execute.DefaultCommandExecutor;
import st.networkers.rimor.extension.ExtensionManager;
import st.networkers.rimor.extension.ExtensionManagerImpl;
import st.networkers.rimor.extension.RimorExtension;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.inject.RimorInjectorImpl;
import st.networkers.rimor.inject.provide.support.OptionalProvider;
import st.networkers.rimor.resolve.PathResolverImpl;
import st.networkers.rimor.inject.provide.ProviderRegistry;
import st.networkers.rimor.inject.provide.RimorProvider;
import st.networkers.rimor.resolve.PathResolver;

import java.util.Objects;

public class Rimor {

    private final RimorInjector injector;

    private final CommandRegistry commandRegistry;
    private final ExtensionManager extensionManager;
    private final ProviderRegistry providerRegistry;

    private final CommandExecutor commandExecutor;
    private final CommandResolver commandResolver;
    private final PathResolver pathResolver;

    private boolean initialized = false;

    public Rimor() {
        this(new ProviderRegistry());
    }

    public Rimor(ProviderRegistry providerRegistry) {
        this(new RimorInjectorImpl(providerRegistry), providerRegistry);
    }

    public Rimor(RimorInjector injector, ProviderRegistry providerRegistry) {
        this(injector, new CommandRegistry(), new ExtensionManagerImpl(), providerRegistry,
                new DefaultCommandExecutor(injector), new CommandResolver(), new PathResolverImpl());
    }

    public Rimor(RimorInjector injector, CommandRegistry commandRegistry, ExtensionManager extensionManager,
                 ProviderRegistry providerRegistry, CommandExecutor commandExecutor, CommandResolver commandResolver,
                 PathResolver pathResolver) {
        this.injector = injector;
        this.commandRegistry = commandRegistry;
        this.extensionManager = extensionManager;
        this.providerRegistry = providerRegistry;
        this.commandExecutor = commandExecutor;
        this.commandResolver = commandResolver;
        this.pathResolver = pathResolver;

        this.registerProvider(new OptionalProvider(injector));
    }

    /**
     * Registers the given {@link RimorCommand}.
     *
     * @param command the command to register
     */
    public Rimor registerCommand(RimorCommand command) {
        Objects.requireNonNull(command);
        commandRegistry.registerCommand(commandResolver.resolve(command));
        return this;
    }

    /**
     * Registers the given {@link RimorCommand}s.
     *
     * @param commands the commands to register
     */
    public Rimor registerCommands(RimorCommand... commands) {
        Objects.requireNonNull(commands);
        for (RimorCommand command : commands)
            this.registerCommand(command);
        return this;
    }

    /**
     * Registers the given {@link RimorProvider}.
     *
     * @param provider the provider to register into the injector
     */
    public Rimor registerProvider(RimorProvider<?> provider) {
        this.providerRegistry.register(provider);
        return this;
    }

    /**
     * Registers the given {@link RimorProvider}s.
     *
     * @param providers the providers to register into the injector
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
        if (this.isInitialized())
            throw new IllegalStateException("this Rimor instance has already been initialized!");

        this.extensionManager.initialize();
        this.initialized = true;
        return this;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public RimorInjector getInjector() {
        return injector;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public ExtensionManager getExtensionManager() {
        return extensionManager;
    }

    public ProviderRegistry getProviderRegistry() {
        return providerRegistry;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public CommandResolver getMappedCommandResolver() {
        return commandResolver;
    }

    public PathResolver getPathResolver() {
        return pathResolver;
    }
}
