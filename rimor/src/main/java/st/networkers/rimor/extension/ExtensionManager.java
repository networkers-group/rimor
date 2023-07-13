package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;

import java.util.Collection;

public interface ExtensionManager {

    void registerExtension(Rimor rimor, RimorExtension extension);

    <T extends RimorExtension> T getExtension(Class<T> extensionClass);

    Collection<RimorExtension> getRegisteredExtensions();

}
