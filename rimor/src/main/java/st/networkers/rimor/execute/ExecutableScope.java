package st.networkers.rimor.execute;

import st.networkers.rimor.aop.AdviceRegistry;
import st.networkers.rimor.aop.exception.ExceptionHandlerRegistry;
import st.networkers.rimor.provide.ProviderRegistry;

import java.util.Objects;

public class ExecutableScope implements Cloneable {

    private ExceptionHandlerRegistry exceptionHandlerRegistry;
    private AdviceRegistry adviceRegistry;
    private ProviderRegistry providerRegistry;

    public ExecutableScope() {
        this(new ExceptionHandlerRegistry(), new AdviceRegistry(advice, exceptionHandlers), new ProviderRegistry());
    }

    public ExecutableScope(ExceptionHandlerRegistry exceptionHandlerRegistry, AdviceRegistry adviceRegistry,
                           ProviderRegistry providerRegistry) {
        this.exceptionHandlerRegistry = exceptionHandlerRegistry;
        this.adviceRegistry = adviceRegistry;
        this.providerRegistry = providerRegistry;
    }

    public AdviceRegistry getExecutionTaskRegistry() {
        return adviceRegistry;
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
        if (!(o instanceof ExecutableScope)) return false;
        ExecutableScope that = (ExecutableScope) o;
        return Objects.equals(exceptionHandlerRegistry, that.exceptionHandlerRegistry) && Objects.equals(adviceRegistry, that.adviceRegistry) && Objects.equals(providerRegistry, that.providerRegistry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exceptionHandlerRegistry, adviceRegistry, providerRegistry);
    }

    @Override
    public ExecutableScope clone() {
        try {
            ExecutableScope clone = (ExecutableScope) super.clone();
            clone.exceptionHandlerRegistry = this.exceptionHandlerRegistry.clone();
            clone.adviceRegistry = this.adviceRegistry.clone();
            clone.providerRegistry = this.providerRegistry.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
