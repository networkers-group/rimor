package st.networkers.rimor.plugin;

import java.util.Collection;

public interface PluginManager {

    void registerPlugin(RimorPlugin plugin);

    <T extends RimorPlugin> T getPlugin(Class<T> pluginClass);

    void unregisterPlugin(RimorPlugin plugin);

    Collection<RimorPlugin> getRegisteredPlugins();

}
