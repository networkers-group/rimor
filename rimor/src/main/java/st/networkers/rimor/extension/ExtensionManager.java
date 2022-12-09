package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.extension.event.RimorEvent;

import java.util.Collection;

public interface ExtensionManager {

    void registerExtension(Rimor rimor, RimorExtension extension);

    <T extends RimorExtension> T getExtension(Class<T> extensionClass);

    void callEvent(RimorEvent event);

    Collection<RimorExtension> getRegisteredExtensions();

}
