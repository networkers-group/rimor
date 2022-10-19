package st.networkers.rimor.plugin;

import java.util.ArrayList;
import java.util.Collection;

public class PluginRegistryImpl implements PluginRegistry {

    private final Collection<RimorPlugin> plugins = new ArrayList<>();

    @Override
    public void registerPlugin(RimorPlugin plugin) {
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
