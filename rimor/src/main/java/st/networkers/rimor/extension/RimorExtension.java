package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.extension.event.RimorEvent;
import st.networkers.rimor.extension.event.RimorEventListener;
import st.networkers.rimor.provide.RimorProvider;

import java.util.Collection;
import java.util.Map;

/**
 * @see AbstractRimorExtension
 */
public interface RimorExtension {

    void configure(Rimor rimor);

    Collection<Command> getCommands();

    Collection<RimorProvider<?>> getProviders();

    Map<Class<? extends RimorEvent>, Collection<RimorEventListener<?>>> getEventListeners();

}
