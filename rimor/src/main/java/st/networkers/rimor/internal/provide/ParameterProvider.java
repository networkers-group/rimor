package st.networkers.rimor.internal.provide;

import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.util.InjectionUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParameterProvider {

    private final Object providerClassInstance;
    private final CachedMethod providerMethod;

    private final List<Annotation> annotations;
    private final List<Class<? extends Annotation>> requiredAnnotations;

    public ParameterProvider(Object providerClassInstance, CachedMethod providerMethod) {
        this.providerClassInstance = providerClassInstance;
        this.providerMethod = providerMethod;
        this.annotations = providerMethod.getAnnotations().stream()
                .filter(annotation -> !(annotation instanceof ProvidesParameter))
                .filter(annotation -> !(annotation instanceof RequireAnnotations))
                .collect(Collectors.toList());
        this.requiredAnnotations = providerMethod.isAnnotationPresent(RequireAnnotations.class)
                ? Arrays.asList(providerMethod.getAnnotation(RequireAnnotations.class).value())
                : Collections.emptyList();
    }

    public boolean canProvide(CachedParameter parameter) {
        return providerMethod.getMethod().getReturnType().isAssignableFrom(parameter.getType())
               && hasRequiredAnnotations(parameter);
    }

    public Object get(CachedParameter parameterToInject,
                      ExecutionContext context,
                      ParameterProviderRegistry parameterProviderRegistry) {
        return InjectionUtils.invokeMethod(
                this.providerMethod,
                this.providerClassInstance,
                this.getContextWithParameterAnnotations(context, parameterToInject),
                parameterProviderRegistry
        );
    }

    private boolean hasRequiredAnnotations(CachedParameter parameter) {
        for (Annotation annotation : this.annotations)
            if (!parameter.getAnnotations().contains(annotation))
                return false;

        for (Class<? extends Annotation> annotationClass : this.requiredAnnotations)
            if (!parameter.isAnnotationPresent(annotationClass))
                return false;

        return this.annotations.size() + this.requiredAnnotations.size() == parameter.getAnnotations().size();
    }

    private ExecutionContext getContextWithParameterAnnotations(ExecutionContext context, CachedParameter parameter) {
        context = context.clone();

        for (Annotation annotation : parameter.getAnnotations())
            context.addComponent(new ContextComponent(annotation.annotationType(), annotation));

        return context;
    }
}
