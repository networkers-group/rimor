package st.networkers.rimor.plugin;

import java.util.Collection;

public interface PluginManager {

    void registerPlugin(RimorPlugin plugin);

    void unregisterPlugin(RimorPlugin plugin);

    Collection<RimorPlugin> getRegisteredPlugins();

}
