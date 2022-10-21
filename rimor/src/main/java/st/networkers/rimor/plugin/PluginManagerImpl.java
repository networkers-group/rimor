package st.networkers.rimor.plugin;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.plugin.event.RimorEvent;
import st.networkers.rimor.plugin.event.RimorEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PluginManagerImpl implements PluginManager {

    private final Rimor rimor;

    private final Map<Class<? extends RimorPlugin>, RimorPlugin> plugins = new HashMap<>();
    private final Map<Class<? extends RimorEvent>, Collection<RimorEventListener<?>>> listeners = new HashMap<>();

    public PluginManagerImpl(Rimor rimor) {
        this.rimor = rimor;
    }

    @Override
    public void registerPlugin(RimorPlugin plugin) {
        plugin.configure(rimor);
        plugin.getCommands().forEach(rimor::registerCommand);
        plugin.getProviders().forEach(rimor::registerProvider);
        this.listeners.putAll(plugin.getEventListeners());
        this.plugins.put(plugin.getClass(), plugin);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RimorPlugin> T getPlugin(Class<T> pluginClass) {
        return (T) this.plugins.get(pluginClass);
    }

    @Override
    public void unregisterPlugin(RimorPlugin plugin) {
        this.plugins.remove(plugin.getClass());
    }

    @Override
    public void callEvent(RimorEvent event) {
        for (RimorEventListener<?> listener : this.listeners.get(event.getClass()))
            this.callEvent(listener, event);
    }

    @SuppressWarnings("unchecked")
    private <T extends RimorEvent> void callEvent(RimorEventListener<T> listener, RimorEvent event) {
        listener.onEvent((T) event);
    }

    @Override
    public Collection<RimorPlugin> getRegisteredPlugins() {
        return this.plugins.values();
    }
}
