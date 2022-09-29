package st.networkers.rimor.internal.provide;

import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ParameterProviderWrapper;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParameterProvider {

    private final ParameterProviderWrapper wrapper;
    private final CachedMethod method;

    private final List<Annotation> annotations;
    private final List<Class<? extends Annotation>> requiredAnnotations;

    public ParameterProvider(ParameterProviderWrapper wrapper, CachedMethod method) {
        this.wrapper = wrapper;
        this.method = method;
        this.annotations = method.getAnnotations().stream()
                .filter(annotation -> !(annotation instanceof ProvidesParameter))
                .filter(annotation -> !(annotation instanceof RequireAnnotations))
                .collect(Collectors.toList());
        this.requiredAnnotations = method.isAnnotationPresent(RequireAnnotations.class)
                ? Arrays.asList(method.getAnnotation(RequireAnnotations.class).value())
                : Collections.emptyList();
    }

    public boolean canProvide(CachedParameter parameter) {
        return parameter.getType().isAssignableFrom(method.getMethod().getReturnType())
               && hasRequiredAnnotations(parameter);
    }

    public Object get(CachedParameter parameterToInject, ExecutionContext context, Injector injector) {
        return injector.invokeMethod(this.method, this.wrapper,
                this.getContextWithParameterAnnotations(context, parameterToInject));
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
