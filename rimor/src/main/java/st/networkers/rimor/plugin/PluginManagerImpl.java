package st.networkers.rimor.plugin;

import st.networkers.rimor.Rimor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PluginManagerImpl implements PluginManager {

    private final Rimor rimor;
    private final Map<Class<? extends RimorPlugin>, RimorPlugin> plugins = new HashMap<>();

    public PluginManagerImpl(Rimor rimor) {
        this.rimor = rimor;
    }

    @Override
    public void registerPlugin(RimorPlugin plugin) {
        plugin.configure(rimor);
        plugin.getCommands().forEach(rimor::registerCommand);
        plugin.getProviders().forEach(rimor::registerProvider);
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
    public Collection<RimorPlugin> getRegisteredPlugins() {
        return this.plugins.values();
    }
}
