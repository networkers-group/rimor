package st.networkers.rimor.instruction;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

public class HandlerMethodInstruction implements Instruction {

    public static Builder builder() {
        return new Builder();
    }

    private final ExecutionContextService executionContextService;

    private final Object bean;
    private final QualifiedMethod qualifiedMethod;
    private final Collection<String> identifiers;

    public HandlerMethodInstruction(ExecutionContextService executionContextService, Object bean,
                                    QualifiedMethod qualifiedMethod, Collection<String> identifiers) {
        this.executionContextService = executionContextService;
        this.bean = bean;
        this.qualifiedMethod = qualifiedMethod;
        this.identifiers = identifiers;
    }

    @Override
    public Object run(ExecutionContext executionContext) {
        return executionContextService.invokeMethod(qualifiedMethod, bean, executionContext);
    }

    public Object getBean() {
        return bean;
    }

    public QualifiedMethod getQualifiedMethod() {
        return qualifiedMethod;
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
        return Objects.equals(bean, that.bean) && Objects.equals(qualifiedMethod, that.qualifiedMethod) && Objects.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bean, qualifiedMethod, identifiers);
    }

    public static class Builder {
        private ExecutionContextService executionContextService;
        private Object bean;
        private Method method;
        private Collection<String> identifiers;

        public Builder setExecutionContextService(ExecutionContextService executionContextService) {
            this.executionContextService = executionContextService;
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
            return new HandlerMethodInstruction(executionContextService, bean, QualifiedMethod.build(method), identifiers);
        }
    }
}
