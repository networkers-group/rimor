package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.provide.RimorProvider;

public abstract class AbstractRimorExtension implements RimorExtension {

    private Rimor rimor = null;

    @Override
    public void configure(Rimor rimor) {
        this.rimor = rimor;
        this.configure();
    }

    /**
     * Called when the extension is going to be registered into a {@link Rimor} instance.
     * <p>
     * This is the right place to register the extension commands, providers or execution tasks.
     */
    protected abstract void configure();

    protected Rimor getRimor() {
        return rimor;
    }

    /**
     * Registers the given {@link RimorCommand}.
     *
     * @param command the command to register
     */
    protected void registerCommand(RimorCommand command) {
        checkRimorState();
        this.rimor.registerCommand(command);
    }

    /**
     * Registers the given {@link RimorCommand}s.
     *
     * @param commands the commands to register
     */
    protected void registerCommands(RimorCommand... commands) {
        checkRimorState();
        this.rimor.registerCommands(commands);
    }

    /**
     * Registers the given provider.
     *
     * @param provider the provider to register into this injector
     */
    protected void registerProvider(RimorProvider<?> provider) {
        checkRimorState();
        this.rimor.registerProvider(provider);
    }

    /**
     * Registers the given providers.
     *
     * @param providers the providers to register into this injector
     */
    protected void registerProviders(RimorProvider<?>... providers) {
        checkRimorState();
        this.rimor.registerProviders(providers);
    }

    // TODO execution tasks

    private void checkRimorState() {
        String msg = "Register commands, providers and execution tasks inside the #configure() method!";
        if (this.rimor == null)
            throw new IllegalStateException("this extension has not been configured yet! " + msg);
        if (this.rimor.isInitialized())
            throw new IllegalStateException("this extension has already been configured! " + msg);
    }
}
