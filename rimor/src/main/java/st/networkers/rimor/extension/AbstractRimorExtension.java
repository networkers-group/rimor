package st.networkers.rimor.extension;

import st.networkers.rimor.command.CommandDefinition;
import st.networkers.rimor.extension.event.RimorEvent;
import st.networkers.rimor.extension.event.RimorEventListener;
import st.networkers.rimor.provide.RimorProvider;

import java.util.*;

public abstract class AbstractRimorExtension implements RimorExtension {

    private final Collection<CommandDefinition> commands = new ArrayList<>();
    private final Collection<RimorProvider<?>> providers = new ArrayList<>();
    private final Map<Class<? extends RimorEvent>, Collection<RimorEventListener<?>>> listeners = new HashMap<>();

    /**
     * Registers the given {@link CommandDefinition}.
     *
     * @param command the command to register
     */
    protected void registerCommand(CommandDefinition command) {
        this.commands.add(command);
    }

    /**
     * Registers the given {@link CommandDefinition}s.
     *
     * @param commands the commands to register
     */
    protected void registerCommands(CommandDefinition... commands) {
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
    public Collection<CommandDefinition> getCommands() {
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
