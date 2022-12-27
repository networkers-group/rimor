package st.networkers.rimor.execute.exception;

public interface ExceptionHandlerRegistry {

    /**
     * Registers the given {@link ExceptionHandler}.
     *
     * @param handler the exception handler to register
     */
    void registerExceptionHandler(ExceptionHandler<?> handler);

    /**
     * Delegates the throwable to the registered {@link ExceptionHandler}s. If there are no registered handlers for the
     * class of the given throwable, it will be thrown.
     *
     * @param throwable the throwable to handle
     */
    <T extends Throwable> void handleException(T throwable);
}
