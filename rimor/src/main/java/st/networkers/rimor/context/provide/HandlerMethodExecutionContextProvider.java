package st.networkers.rimor.context.provide;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class HandlerMethodExecutionContextProvider implements ExecutionContextProvider<Object> {

    private final ExecutionContextService executionContextService;

    private final Object bean;
    private final QualifiedMethod method;
    private final Collection<Type> providedTypes;

    public HandlerMethodExecutionContextProvider(ExecutionContextService executionContextService,
                                                 Object bean, QualifiedMethod method, Collection<Type> providedTypes) {
        this.executionContextService = executionContextService;
        this.bean = bean;
        this.method = method;
        this.providedTypes = providedTypes;
    }

    @Override
    public Object get(Token<Object> token, ExecutionContext context) {
        // first, create a context copy with the token and annotations present in this method to make it possible to inject them:
        ExecutionContext.Builder contextWithToken = ExecutionContext.builder()
                .copy(context)
                .bind(new Token<Token<?>>() {}, token);

        for (Map.Entry<Class<? extends Annotation>, Annotation> entry : token.getQualifiersMap().entrySet()) {
            contextWithToken.bind(Token.of((Type) entry.getKey()), entry.getValue());
        }

        return executionContextService.invokeMethod(method, bean, contextWithToken.build());
    }

    public Object getBean() {
        return bean;
    }

    public QualifiedMethod getMethod() {
        return method;
    }

    @Override
    public Collection<Type> getProvidedTypes() {
        return providedTypes;
    }

    @Override
    public Map<Class<? extends Annotation>, Annotation> getQualifiersMap() {
        return method.getQualifiersMap();
    }

    @Override
    public Collection<Class<? extends Annotation>> getRequiredQualifiers() {
        return method.getRequiredQualifiers();
    }

    @Override
    public String toString() {
        return this.method.getMethod().toString() + " handler method";
    }
}
