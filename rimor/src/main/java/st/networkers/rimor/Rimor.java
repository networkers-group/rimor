package st.networkers.rimor;

import st.networkers.rimor.bean.BeanManager;
import st.networkers.rimor.command.CommandRegistry;
import st.networkers.rimor.execute.CommandExecutor;
import st.networkers.rimor.extension.ExtensionManager;
import st.networkers.rimor.extension.RimorExtension;
import st.networkers.rimor.extension.SupportExtension;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.inject.provide.ProviderRegistry;
import st.networkers.rimor.inject.provide.RimorProvider;
import st.networkers.rimor.resolve.PathResolver;

public class Rimor {

    private final BeanManager beanManager;
    private final CommandRegistry commandRegistry;
    private final CommandExecutor commandExecutor;
    private final ExtensionManager extensionManager;
    private final ProviderRegistry providerRegistry;
    private final PathResolver pathResolver;
    private final RimorInjector injector;

    public Rimor(BeanManager beanManager, CommandRegistry commandRegistry, CommandExecutor commandExecutor,
                 ExtensionManager extensionManager, ProviderRegistry providerRegistry, PathResolver pathResolver,
                 RimorInjector injector) {
        this.beanManager = beanManager;
        this.commandRegistry = commandRegistry;
        this.commandExecutor = commandExecutor;
        this.extensionManager = extensionManager;
        this.providerRegistry = providerRegistry;
        this.pathResolver = pathResolver;
        this.injector = injector;

        this.registerExtension(new SupportExtension());
    }

    public Rimor register(Object bean) {
        this.beanManager.processBean(bean);
        return this;
    }

    /**
     * Registers the given {@link RimorProvider}.
     *
     * @param provider the provider to register
     */
    public Rimor registerProvider(RimorProvider<?> provider) {
        this.providerRegistry.register(provider);
        return this;
    }

    /**
     * Registers the given {@link RimorProvider}s.
     *
     * @param providers the providers to register
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
     * Registers the given {@link RimorExtension}s.
     *
     * @param extensions the extensions to register
     */
    public Rimor registerExtensions(RimorExtension... extensions) {
        for (RimorExtension extension : extensions)
            this.registerExtension(extension);
        return this;
    }

    public BeanManager getBeanManager() {
        return beanManager;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    public ExtensionManager getExtensionManager() {
        return extensionManager;
    }

    public ProviderRegistry getProviderRegistry() {
        return providerRegistry;
    }

    public PathResolver getPathResolver() {
        return pathResolver;
    }

    public RimorInjector getInjector() {
        return injector;
    }
}
