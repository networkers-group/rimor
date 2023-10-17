package st.networkers.rimor.context.provide;

import org.apache.commons.lang3.reflect.TypeUtils;
import st.networkers.rimor.bean.BeanProcessingException;
import st.networkers.rimor.bean.BeanProcessor;
import st.networkers.rimor.bean.RimorConfiguration;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ProvidesContextProcessor implements BeanProcessor {

    private final ExecutionContextService executionContextService;

    public ProvidesContextProcessor(ExecutionContextService executionContextService) {
        this.executionContextService = executionContextService;
    }

    @Override
    public void process(Object bean) {
        Arrays.stream(bean.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(ProvidesContext.class))
                .map(method -> this.processMethod(method, bean))
                .forEach(contextProvider -> this.registerContextProvider(contextProvider, bean));
    }

    public HandlerMethodExecutionContextProvider processMethod(Method method, Object bean) {
        ProvidesContext providesContext = method.getAnnotation(ProvidesContext.class);
        QualifiedMethod qualifiedMethod = QualifiedMethod.build(method);

        Type methodReturnType = method.getGenericReturnType();
        Collection<Type> providedTypes = providesContext.value().length > 0
                ? Arrays.asList(providesContext.value())
                : Collections.singletonList(methodReturnType);

        for (Type providedType : providedTypes) {
            if (providedType.equals(Void.TYPE)) {
                throw new BeanProcessingException(bean, "execution context provider handler method " + method + " cannot provide void!");
            }

            if (!TypeUtils.isAssignable(methodReturnType, providedType)) {
                throw new BeanProcessingException(bean, method + "'s return type is not compatible with provided type " + providedType);
            }
        }

        return new HandlerMethodExecutionContextProvider(executionContextService, bean, qualifiedMethod, providedTypes);
    }

    // package-private for testing purposes
    void registerContextProvider(HandlerMethodExecutionContextProvider contextProvider, Object bean) {
        boolean registerGlobally = bean.getClass().isAnnotationPresent(RimorConfiguration.class);

        try {
            if (registerGlobally) {
                executionContextService.registerGlobalExecutionContextProvider(contextProvider);
            } else {
                executionContextService.registerExecutionContextProvider(contextProvider, bean);
            }
        } catch (IllegalStateException e) {
            throw new BeanProcessingException(bean, e);
        }
    }
}
