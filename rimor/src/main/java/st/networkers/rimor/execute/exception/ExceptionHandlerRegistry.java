package st.networkers.rimor.execute.exception;

import st.networkers.rimor.context.ExecutionContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlerRegistry implements Cloneable {

    private Map<Class<?>, Collection<ExceptionHandler<?>>> handlers;

    public ExceptionHandlerRegistry() {
        this(new HashMap<>());
    }

    public ExceptionHandlerRegistry(Map<Class<?>, Collection<ExceptionHandler<?>>> handlers) {
        this.handlers = handlers;
    }

    /**
     * Registers the given {@link ExceptionHandler}.
     *
     * @param handler the exception handler to register
     */
    public void registerExceptionHandler(ExceptionHandler<?> handler) {
        for (Class<?> handledType : handler.getHandledTypes())
            this.handlers.computeIfAbsent(handledType, t -> new ArrayList<>()).add(handler);
    }

    /**
     * Delegates the throwable to the registered {@link ExceptionHandler}s. If there are no registered handlers for the
     * class of the given throwable, it will be rethrown.
     *
     * @param throwable the throwable to handle
     */
    public <T extends Throwable> void handleException(T throwable, ExecutionContext executionContext) {
        Collection<ExceptionHandler<?>> handlers = this.handlers.get(throwable.getClass());
        if (handlers == null)
            throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);

        handlers.forEach(handler -> this.handleException(handler, throwable, executionContext));
    }

    @SuppressWarnings("unchecked")
    private <T extends Throwable> void handleException(ExceptionHandler<T> handler, Throwable throwable, ExecutionContext executionContext) {
        handler.handle((T) throwable, executionContext);
    }

    @Override
    public ExceptionHandlerRegistry clone() {
        try {
            ExceptionHandlerRegistry clone = (ExceptionHandlerRegistry) super.clone();
            clone.handlers = new HashMap<>(this.handlers);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
