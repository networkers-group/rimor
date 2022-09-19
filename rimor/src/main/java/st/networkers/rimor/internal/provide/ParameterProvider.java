package st.networkers.rimor.internal.provide;

import org.jetbrains.annotations.Nullable;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.util.InjectionUtils;
import st.networkers.rimor.util.ReflectionUtils;

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
               && parameter.getAnnotations().containsAll(this.annotations)
               && hasRequiredAnnotations(parameter);
    }

    public Object get(CachedParameter parameterToInject, ExecutionContext context) {
        Object[] providerParameters = InjectionUtils.resolve(
                this.providerMethod,
                context,
                parameter -> getAnnotation(parameterToInject, parameter.getType())
        );

        return ReflectionUtils.invoke(this.providerMethod.getMethod(), this.providerClassInstance, providerParameters);
    }

    @SuppressWarnings("unchecked")
    private @Nullable Annotation getAnnotation(CachedParameter parameterToInject, Class<?> annotationClass) {
        if (!Annotation.class.isAssignableFrom(annotationClass))
            return null;

        return parameterToInject.getAnnotation((Class<? extends Annotation>) annotationClass);
    }

    private boolean hasRequiredAnnotations(CachedParameter parameter) {
        for (Class<? extends Annotation> annotationClass : this.requiredAnnotations)
            if (!parameter.isAnnotationPresent(annotationClass))
                return false;
        return true;
    }
}
