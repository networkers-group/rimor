package st.networkers.rimor.plugin;

import st.networkers.rimor.Rimor;

import java.util.ArrayList;
import java.util.Collection;

public class PluginManagerImpl implements PluginManager {

    private final Rimor rimor;
    private final Collection<RimorPlugin> plugins = new ArrayList<>();

    public PluginManagerImpl(Rimor rimor) {
        this.rimor = rimor;
    }

    @Override
    public void registerPlugin(RimorPlugin plugin) {
        plugin.configure(rimor);
        plugin.getCommands().forEach(rimor::registerCommand);
        plugin.getProviders().forEach(rimor::registerProvider);
        plugins.add(plugin);
    }

    @Override
    public void unregisterPlugin(RimorPlugin plugin) {
        plugins.remove(plugin);
    }

    @Override
    public Collection<RimorPlugin> getRegisteredPlugins() {
        return this.plugins;
    }
}
