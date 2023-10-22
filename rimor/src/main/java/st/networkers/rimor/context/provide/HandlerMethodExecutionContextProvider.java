package st.networkers.rimor.context.provide;

import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.qualify.ImmutableToken;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;
import st.networkers.rimor.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandlerMethodExecutionContextProvider implements ExecutionContextProvider<Object> {

    public static HandlerMethodExecutionContextProvider build(ExecutionContextService executionContextService,
                                                              Object bean, QualifiedMethod qualifiedMethod,
                                                              Collection<Type> providedTypes) {
        List<Type> curatedProvidedTypes = providedTypes.stream()
                .map(ReflectionUtils::wrapPrimitive)
                .collect(Collectors.toList());

        return new HandlerMethodExecutionContextProvider(executionContextService, bean, qualifiedMethod, curatedProvidedTypes);
    }

    private final ExecutionContextService executionContextService;

    private final Object bean;
    private final QualifiedMethod method;
    private final Collection<Type> providedTypes;

    private HandlerMethodExecutionContextProvider(ExecutionContextService executionContextService,
                                                  Object bean, QualifiedMethod method, Collection<Type> providedTypes) {
        this.executionContextService = executionContextService;
        this.bean = bean;
        this.method = method;
        this.providedTypes = providedTypes;
    }

    @Override
    public Object get(Token token, ExecutionContext context) {
        // first, create a context copy with the token and annotations present in the handler method to make it possible
        // to inject them:
        ExecutionContext.Builder executionContextBuilder = ExecutionContext.builder()
                .withBindingsOf(context)
                .bind(Token.class, ImmutableToken.copyOf(token));
        token.getQualifiersMap().forEach(executionContextBuilder::bind);

        ExecutionContext executionContext = executionContextBuilder.build();
        return executionContextService.invokeMethod(method, bean, executionContext);
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
