package st.networkers.rimor.plugin;

import st.networkers.rimor.plugin.event.RimorEvent;

import java.util.Collection;

public interface PluginManager {

    void registerPlugin(RimorPlugin plugin);

    <T extends RimorPlugin> T getPlugin(Class<T> pluginClass);

    void unregisterPlugin(RimorPlugin plugin);

    void callEvent(RimorEvent event);

    Collection<RimorPlugin> getRegisteredPlugins();

}
