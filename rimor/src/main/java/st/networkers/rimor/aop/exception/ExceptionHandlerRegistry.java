package st.networkers.rimor.aop.exception;

import st.networkers.rimor.aop.ExceptionHandler;
import st.networkers.rimor.execute.ExecutableScope;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.instruction.Instruction;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlerRegistry implements Cloneable {

    private Map<Class<?>, ExceptionHandler<?>> handlers;

    public ExceptionHandlerRegistry() {
        this(new HashMap<>());
    }

    public ExceptionHandlerRegistry(Map<Class<?>, ExceptionHandler<?>> handlers) {
        this.handlers = handlers;
    }

    /**
     * Registers the given {@link ExceptionHandler}.
     *
     * @param handler the exception handler to register
     */
    public void registerExceptionHandler(ExceptionHandler<?> handler) {
        for (Class<?> handledType : handler.getHandledTypes())
            this.handlers.putIfAbsent(handledType, handler);
    }

    /**
     * Delegates the throwable to the registered {@link ExceptionHandler}s. If there are no registered handlers for the
     * class of the given throwable, it will be rethrown.
     *
     * @param throwable the throwable to handle
     */
    @SuppressWarnings("unchecked")
    public <T extends Throwable> void handleException(T throwable, Instruction instruction,
                                                      ExecutableScope executableScope, ExecutionContext executionContext) {
        ExceptionHandler<?> handler = executableScope.getExceptionHandlerRegistry().handlers.get(throwable.getClass());

        if (handler == null)
            handler = this.handlers.get(throwable.getClass());

        if (handler != null)
            ((ExceptionHandler<T>) handler).handle(throwable, instruction, executableScope, executionContext);

        throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
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
