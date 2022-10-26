package st.networkers.rimor.plugin;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.plugin.event.RimorEvent;
import st.networkers.rimor.plugin.event.RimorEventListener;
import st.networkers.rimor.provide.RimorProvider;

import java.util.Collection;
import java.util.Map;

/**
 * @see AbstractRimorPlugin
 */
public interface RimorPlugin {

    void configure(Rimor rimor);

    Collection<Command> getCommands();

    Collection<RimorProvider<?>> getProviders();

    Map<Class<? extends RimorEvent>, Collection<RimorEventListener<?>>> getEventListeners();

}
