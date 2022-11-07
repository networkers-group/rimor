package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.extension.event.RimorEvent;
import st.networkers.rimor.extension.event.RimorEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExtensionManagerImpl implements ExtensionManager {

    private final Rimor rimor;

    private final Map<Class<? extends RimorExtension>, RimorExtension> extensions = new HashMap<>();
    private final Map<Class<? extends RimorEvent>, Collection<RimorEventListener<?>>> listeners = new HashMap<>();

    public ExtensionManagerImpl(Rimor rimor) {
        this.rimor = rimor;
    }

    @Override
    public void registerExtension(RimorExtension extension) {
        extension.configure(rimor);
        extension.getCommands().forEach(rimor::registerCommand);
        extension.getProviders().forEach(rimor::registerProvider);
        this.listeners.putAll(extension.getEventListeners());
        this.extensions.put(extension.getClass(), extension);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RimorExtension> T getExtension(Class<T> extensionClass) {
        return (T) this.extensions.get(extensionClass);
    }

    @Override
    public void callEvent(RimorEvent event) {
        for (RimorEventListener<?> listener : this.listeners.get(event.getClass()))
            this.callEvent(listener, event);
    }

    @SuppressWarnings("unchecked")
    private <T extends RimorEvent> void callEvent(RimorEventListener<T> listener, RimorEvent event) {
        listener.onEvent((T) event);
    }

    @Override
    public Collection<RimorExtension> getRegisteredExtensions() {
        return this.extensions.values();
    }
}
