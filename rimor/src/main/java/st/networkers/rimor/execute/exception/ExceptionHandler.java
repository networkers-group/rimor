package st.networkers.rimor.execute.exception;

import st.networkers.rimor.context.ExecutionContext;

import java.util.Collection;

/**
 * @see AbstractExceptionHandler
 * @see st.networkers.rimor.Rimor#registerExceptionHandler(ExceptionHandler)
 */
public interface ExceptionHandler<T extends Throwable> {

    Collection<Class<? extends T>> getHandledTypes();

    void handle(T throwable, ExecutionContext executionContext);

}
