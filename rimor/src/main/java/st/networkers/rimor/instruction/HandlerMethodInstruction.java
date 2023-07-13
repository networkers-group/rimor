package st.networkers.rimor.instruction;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.RimorInjector;
import st.networkers.rimor.reflect.CachedMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

public class HandlerMethodInstruction implements Instruction {

    public static Builder builder() {
        return new Builder();
    }

    private final RimorInjector injector;

    private final Object bean;
    private final CachedMethod method;
    private final Collection<String> identifiers;

    public HandlerMethodInstruction(RimorInjector injector, Object bean, CachedMethod method, Collection<String> identifiers) {
        this.injector = injector;
        this.bean = bean;
        this.method = method;
        this.identifiers = identifiers;
    }

    @Override
    public Object run(ExecutionContext executionContext) {
        return injector.invokeMethod(method, bean, executionContext);
    }

    public Object getBean() {
        return bean;
    }

    public CachedMethod getMethod() {
        return method;
    }

    @Override
    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HandlerMethodInstruction)) return false;
        HandlerMethodInstruction that = (HandlerMethodInstruction) o;
        return Objects.equals(bean, that.bean) && Objects.equals(method, that.method) && Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bean, method, identifiers);
    }

    public static class Builder {
        private RimorInjector injector;
        private Object bean;
        private Method method;
        private Collection<String> identifiers;

        public Builder injector(RimorInjector injector) {
            this.injector = injector;
            return this;
        }

        public Builder bean(Object bean) {
            this.bean = bean;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder identifiers(Collection<String> identifiers) {
            this.identifiers = identifiers;
            return this;
        }

        public HandlerMethodInstruction create() {
            return new HandlerMethodInstruction(injector, bean, CachedMethod.build(method), identifiers);
        }
    }
}
