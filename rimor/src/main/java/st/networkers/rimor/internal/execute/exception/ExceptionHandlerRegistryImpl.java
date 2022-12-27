package st.networkers.rimor.internal.execute.exception;

import st.networkers.rimor.execute.exception.ExceptionHandler;
import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlerRegistryImpl implements ExceptionHandlerRegistry {

    private final Map<Class<?>, Collection<ExceptionHandler<?>>> exceptionHandlers = new HashMap<>();

    @Override
    public void registerExceptionHandler(ExceptionHandler<?> handler) {
        for (Class<?> handledType : handler.getHandledTypes())
            this.exceptionHandlers.computeIfAbsent(handledType, t -> new ArrayList<>()).add(handler);
    }

    @Override
    public <T extends Throwable> void handleException(T throwable) {
        Collection<ExceptionHandler<?>> handlers = this.exceptionHandlers.get(throwable.getClass());
        if (handlers == null)
            throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);

        handlers.forEach(handler -> this.handleException(handler, throwable));
    }

    @SuppressWarnings("unchecked")
    private <T extends Throwable> void handleException(ExceptionHandler<T> handler, Throwable throwable) {
        handler.handle((T) throwable);
    }
}
