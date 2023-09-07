package st.networkers.rimor;

import st.networkers.rimor.bean.BeanManager;
import st.networkers.rimor.bean.BeanProcessor;
import st.networkers.rimor.command.CommandRegistry;
import st.networkers.rimor.execute.CommandExecutor;
import st.networkers.rimor.extension.ExtensionManager;
import st.networkers.rimor.extension.RimorExtension;
import st.networkers.rimor.extension.SupportExtension;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.context.provide.ExecutionContextProviderRegistry;
import st.networkers.rimor.context.provide.ExecutionContextProvider;
import st.networkers.rimor.resolve.PathResolver;

public class Rimor {

    private final BeanManager beanManager;
    private final BeanProcessor beanProcessor;
    private final CommandRegistry commandRegistry;
    private final CommandExecutor commandExecutor;
    private final ExecutionContextProviderRegistry executionContextProviderRegistry;
    private final ExecutionContextService executionContextService;
    private final ExtensionManager extensionManager;
    private final PathResolver pathResolver;

    public Rimor(BeanManager beanManager, BeanProcessor beanProcessor, CommandRegistry commandRegistry, CommandExecutor commandExecutor,
                 ExtensionManager extensionManager, ExecutionContextProviderRegistry executionContextProviderRegistry, PathResolver pathResolver,
                 ExecutionContextService executionContextService) {
        this.beanManager = beanManager;
        this.beanProcessor = beanProcessor;
        this.commandRegistry = commandRegistry;
        this.commandExecutor = commandExecutor;
        this.extensionManager = extensionManager;
        this.executionContextProviderRegistry = executionContextProviderRegistry;
        this.pathResolver = pathResolver;
        this.executionContextService = executionContextService;

        this.registerExtension(new SupportExtension());
    }

    /**
     * Registers the given bean.
     *
     * @param bean the bean register
     */
    public Rimor register(Object bean) {
        this.beanManager.processBean(bean);
        return this;
    }

    /**
     * Registers the given {@link ExecutionContextProvider}.
     *
     * @param provider the provider to register
     */
    public Rimor registerExecutionContextProvider(ExecutionContextProvider<?> provider) {
        this.executionContextProviderRegistry.register(provider);
        return this;
    }

    /**
     * Registers the given {@link ExecutionContextProvider}s.
     *
     * @param providers the providers to register
     */
    public Rimor registerExecutionContextProviders(ExecutionContextProvider<?>... providers) {
        for (ExecutionContextProvider<?> provider : providers)
            this.registerExecutionContextProvider(provider);
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

    public BeanProcessor getBeanProcessor() {
        return beanProcessor;
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

    public ExecutionContextProviderRegistry getProviderRegistry() {
        return executionContextProviderRegistry;
    }

    public PathResolver getPathResolver() {
        return pathResolver;
    }

    public ExecutionContextService getExecutionContextService() {
        return executionContextService;
    }
}
