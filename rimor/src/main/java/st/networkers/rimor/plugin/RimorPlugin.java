package st.networkers.rimor.plugin;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.command.Command;
import st.networkers.rimor.provide.RimorProvider;

import java.util.Collection;

public interface RimorPlugin {

    void configure(Rimor rimor);

    Collection<Command> getCommands();

    Collection<RimorProvider<?>> getProviders();

}
