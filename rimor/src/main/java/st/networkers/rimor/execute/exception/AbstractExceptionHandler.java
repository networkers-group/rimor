package st.networkers.rimor.execute.exception;

import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractExceptionHandler<T extends Throwable> implements ExceptionHandler<T> {

    private final Collection<Class<? extends T>> handledTypes;

    @SafeVarargs
    protected AbstractExceptionHandler(Class<? extends T>... handledTypes) {
        this(Arrays.asList(handledTypes));
    }

    protected AbstractExceptionHandler(Collection<Class<? extends T>> handledTypes) {
        this.handledTypes = handledTypes;
    }

    @Override
    public Collection<Class<? extends T>> getHandledTypes() {
        return this.handledTypes;
    }
}
