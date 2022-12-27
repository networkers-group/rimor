package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExtensionManagerImpl implements ExtensionManager {

    private final Map<Class<? extends RimorExtension>, RimorExtension> extensions = new HashMap<>();

    @Override
    public void registerExtension(Rimor rimor, RimorExtension extension) {
        extension.configure(rimor);
        this.extensions.put(extension.getClass(), extension);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RimorExtension> T getExtension(Class<T> extensionClass) {
        return (T) this.extensions.get(extensionClass);
    }

    @Override
    public Collection<RimorExtension> getRegisteredExtensions() {
        return this.extensions.values();
    }

    @Override
    public void initialize() {
        for (RimorExtension extension : this.getRegisteredExtensions())
            extension.initialize();
    }
}
