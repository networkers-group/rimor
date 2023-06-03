package st.networkers.rimor.executable;

import st.networkers.rimor.execute.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.execute.task.ExecutionTaskRegistry;
import st.networkers.rimor.provide.ProviderRegistry;

import java.util.Objects;

public class ExecutableProperties implements Cloneable {

    private ExceptionHandlerRegistry exceptionHandlerRegistry;
    private ExecutionTaskRegistry executionTaskRegistry;
    private ProviderRegistry providerRegistry;

    public ExecutableProperties() {
        this(new ExceptionHandlerRegistry(), new ExecutionTaskRegistry(), new ProviderRegistry());
    }

    public ExecutableProperties(ExceptionHandlerRegistry exceptionHandlerRegistry, ExecutionTaskRegistry executionTaskRegistry,
                                ProviderRegistry providerRegistry) {
        this.exceptionHandlerRegistry = exceptionHandlerRegistry;
        this.executionTaskRegistry = executionTaskRegistry;
        this.providerRegistry = providerRegistry;
    }

    public ExecutionTaskRegistry getExecutionTaskRegistry() {
        return executionTaskRegistry;
    }

    public ExceptionHandlerRegistry getExceptionHandlerRegistry() {
        return exceptionHandlerRegistry;
    }

    public ProviderRegistry getProviderRegistry() {
        return providerRegistry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutableProperties)) return false;
        ExecutableProperties that = (ExecutableProperties) o;
        return Objects.equals(exceptionHandlerRegistry, that.exceptionHandlerRegistry) && Objects.equals(executionTaskRegistry, that.executionTaskRegistry) && Objects.equals(providerRegistry, that.providerRegistry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exceptionHandlerRegistry, executionTaskRegistry, providerRegistry);
    }

    @Override
    public ExecutableProperties clone() {
        try {
            ExecutableProperties clone = (ExecutableProperties) super.clone();
            clone.exceptionHandlerRegistry = this.exceptionHandlerRegistry.clone();
            clone.executionTaskRegistry = this.executionTaskRegistry.clone();
            clone.providerRegistry = this.providerRegistry.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
