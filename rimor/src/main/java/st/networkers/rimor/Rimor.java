package st.networkers.rimor;

import lombok.Getter;
import st.networkers.rimor.command.CommandRegistry;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.execute.CommandExecutor;
import st.networkers.rimor.execute.exception.ExceptionHandler;
import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.task.ExecutionTask;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.execute.task.PostExecutionTask;
import st.networkers.rimor.execute.task.PreExecutionTask;
import st.networkers.rimor.extension.ExtensionManager;
import st.networkers.rimor.extension.ExtensionManagerImpl;
import st.networkers.rimor.extension.RimorExtension;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.internal.execute.CommandExecutorImpl;
import st.networkers.rimor.internal.execute.exception.ExceptionHandlerRegistryImpl;
import st.networkers.rimor.internal.execute.task.ExecutionTaskRegistryImpl;
import st.networkers.rimor.internal.inject.InjectorImpl;
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
    private final ExceptionHandlerRegistry exceptionHandlerRegistry;
    private final ExecutionTaskRegistry executionTaskRegistry;
    private final ExtensionManager extensionManager;
    private final ProviderRegistry providerRegistry;

    private final Injector injector;
    private final PathResolver pathResolver;
    private final CommandExecutor executor;

    private boolean initialized = false;

    public Rimor() {
        this(new InjectorImpl(new ProviderRegistryImpl()));
    }

    public Rimor(Injector injector) {
        this(new CommandRegistry(), new ExceptionHandlerRegistryImpl(), new ExecutionTaskRegistryImpl(),
                new ExtensionManagerImpl(), injector.getProviderRegistry(), injector);
    }

    public Rimor(CommandRegistry commandRegistry, ExceptionHandlerRegistry exceptionHandlerRegistry,
                 ExecutionTaskRegistry executionTaskRegistry, ExtensionManager extensionManager,
                 ProviderRegistry providerRegistry, Injector injector) {
        this(commandRegistry, exceptionHandlerRegistry, executionTaskRegistry, extensionManager, providerRegistry, injector,
                new PathResolverImpl(exceptionHandlerRegistry, executionTaskRegistry),
                new CommandExecutorImpl(injector, exceptionHandlerRegistry, executionTaskRegistry));
    }

    public Rimor(CommandRegistry commandRegistry, ExceptionHandlerRegistry exceptionHandlerRegistry,
                 ExecutionTaskRegistry executionTaskRegistry, ExtensionManager extensionManager,
                 ProviderRegistry providerRegistry, Injector injector, PathResolver pathResolver,
                 CommandExecutor executor) {
        this.commandRegistry = commandRegistry;
        this.exceptionHandlerRegistry = exceptionHandlerRegistry;
        this.executionTaskRegistry = executionTaskRegistry;
        this.extensionManager = extensionManager;
        this.providerRegistry = providerRegistry;
        this.injector = injector;
        this.pathResolver = pathResolver;
        this.executor = executor;

        this.registerProvider(new OptionalProvider(injector));
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
     * Registers the given {@link ExceptionHandler}.
     *
     * @param handler the exception handler to register
     */
    public Rimor registerExceptionHandler(ExceptionHandler<?> handler) {
        this.exceptionHandlerRegistry.registerExceptionHandler(handler);
        return this;
    }

    /**
     * Registers the given {@link ExceptionHandler}s.
     *
     * @param handlers the exception handlers to register
     */
    public Rimor registerExceptionHandlers(ExceptionHandler<?>... handlers) {
        for (ExceptionHandler<?> handler : handlers)
            this.registerExceptionHandler(handler);
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
     * Registers the given {@link ExecutionTask}.
     *
     * @param executionTask the execution task to register
     */
    public Rimor registerExecutionTask(ExecutionTask executionTask) {
        if (executionTask instanceof PreExecutionTask)
            return this.registerPreExecutionTask((PreExecutionTask) executionTask);
        else if (executionTask instanceof PostExecutionTask)
            return this.registerPostExecutionTask((PostExecutionTask) executionTask);
        throw new IllegalArgumentException(executionTask + " is neither a PreExecutionTask nor a PostExecutionTask");
    }

    /**
     * Registers the given {@link ExecutionTask}s.
     *
     * @param executionTasks the execution tasks to register
     */
    public Rimor registerExecutionTasks(ExecutionTask... executionTasks) {
        for (ExecutionTask executionTask : executionTasks)
            this.registerExecutionTask(executionTask);
        return this;
    }

    /**
     * Registers the given {@link PreExecutionTask}.
     *
     * @param executionTask the pre-execution task to register
     */
    public Rimor registerPreExecutionTask(PreExecutionTask executionTask) {
        this.executionTaskRegistry.registerPreExecutionTask(executionTask);
        return this;
    }

    /**
     * Registers the given {@link PreExecutionTask}s.
     *
     * @param executionTasks the pre-execution tasks to register
     */
    public Rimor registerPreExecutionTasks(PreExecutionTask... executionTasks) {
        for (PreExecutionTask executionTask : executionTasks)
            this.registerPreExecutionTask(executionTask);
        return this;
    }

    /**
     * Registers the given {@link PostExecutionTask}.
     *
     * @param executionTask the post-execution task to register
     */
    public Rimor registerPostExecutionTask(PostExecutionTask executionTask) {
        this.executionTaskRegistry.registerPostExecutionTask(executionTask);
        return this;
    }

    /**
     * Registers the given {@link PostExecutionTask}s.
     *
     * @param executionTasks the post-execution tasks to register
     */
    public Rimor registerPostExecutionTasks(PostExecutionTask... executionTasks) {
        for (PostExecutionTask executionTask : executionTasks)
            this.registerPostExecutionTask(executionTask);
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

    public Object execute(Instruction instruction, ExecutionContext context) {
        return this.executor.execute(instruction, context);
    }
}
