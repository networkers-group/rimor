package st.networkers.rimor.plugin;

import java.util.Collection;

public interface PluginRegistry {

    void registerPlugin(RimorPlugin plugin);

    void unregisterPlugin(RimorPlugin plugin);

    Collection<RimorPlugin> getRegisteredPlugins();

}
