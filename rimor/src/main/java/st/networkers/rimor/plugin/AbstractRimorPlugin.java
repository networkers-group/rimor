package st.networkers.rimor.plugin;

import st.networkers.rimor.command.Command;
import st.networkers.rimor.plugin.event.RimorEvent;
import st.networkers.rimor.plugin.event.RimorEventListener;
import st.networkers.rimor.provide.RimorProvider;

import java.util.*;

public abstract class AbstractRimorPlugin implements RimorPlugin {

    private final Collection<Command> commands = new ArrayList<>();
    private final Collection<RimorProvider<?>> providers = new ArrayList<>();
    private final Map<Class<? extends RimorEvent>, Collection<RimorEventListener<?>>> listeners = new HashMap<>();

    /**
     * Registers the given {@link Command}.
     *
     * @param command the command to register
     */
    protected void registerCommand(Command command) {
        this.commands.add(command);
    }

    /**
     * Registers the given {@link Command}s.
     *
     * @param commands the commands to register
     */
    protected void registerCommands(Command... commands) {
        Collections.addAll(this.commands, commands);
    }

    /**
     * Registers the given provider.
     *
     * @param provider the provider to register into this injector
     */
    protected void registerProvider(RimorProvider<?> provider) {
        this.providers.add(provider);
    }

    /**
     * Registers the given providers.
     *
     * @param providers the providers to register into this injector
     */
    protected void registerProviders(RimorProvider<?>... providers) {
        Collections.addAll(this.providers, providers);
    }

    /**
     * Registers the given {@link RimorEventListener}.
     *
     * @param eventClass the event to listen to
     * @param listener the listener to register
     */
    protected <T extends RimorEvent> void registerListener(Class<T> eventClass, RimorEventListener<T> listener) {
        this.listeners.computeIfAbsent(eventClass, c -> new ArrayList<>()).add(listener);
    }

    @Override
    public Collection<Command> getCommands() {
        return this.commands;
    }

    @Override
    public Collection<RimorProvider<?>> getProviders() {
        return this.providers;
    }

    @Override
    public Map<Class<? extends RimorEvent>, Collection<RimorEventListener<?>>> getEventListeners() {
        return this.listeners;
    }
}
