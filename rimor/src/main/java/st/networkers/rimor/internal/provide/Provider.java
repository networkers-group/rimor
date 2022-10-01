package st.networkers.rimor.internal.provide;

import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.provide.RimorProviderWrapper;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Provider {

    public static Provider build(RimorProviderWrapper wrapper, CachedMethod method) {
        return new Provider(
                wrapper,
                method,
                method.getAnnotations().stream()
                        .filter(annotation -> !(annotation instanceof ProvidesParameter))
                        .filter(annotation -> !(annotation instanceof RequireAnnotations))
                        .collect(Collectors.toList()),
                method.isAnnotationPresent(RequireAnnotations.class)
                        ? Arrays.asList(method.getAnnotation(RequireAnnotations.class).value())
                        : Collections.emptyList()
        );
    }

    private final RimorProviderWrapper wrapper;
    private final CachedMethod method;

    private final List<Annotation> annotations;
    private final List<Class<? extends Annotation>> requiredAnnotations;

    public Provider(RimorProviderWrapper wrapper, CachedMethod method, List<Annotation> annotations,
                    List<Class<? extends Annotation>> requiredAnnotations) {
        this.wrapper = wrapper;
        this.method = method;
        this.annotations = annotations;
        this.requiredAnnotations = requiredAnnotations;
    }

    public boolean canProvide(Token token) {
        return token.getType().isAssignableFrom(method.getMethod().getReturnType()) && hasRequiredAnnotations(token);
    }

    public Object get(Token token, ExecutionContext context, Injector injector) {
        return injector.invokeMethod(this.method, this.wrapper, this.getContextWithToken(context, token));
    }

    private boolean hasRequiredAnnotations(Token token) {
        for (Annotation annotation : this.annotations)
            if (!token.getAnnotations().containsValue(annotation))
                return false;

        for (Class<? extends Annotation> annotationClass : this.requiredAnnotations)
            if (!token.getAnnotations().containsKey(annotationClass))
                return false;

        return this.annotations.size() + this.requiredAnnotations.size() == token.getAnnotations().size();
    }

    private ExecutionContext getContextWithToken(ExecutionContext context, Token token) {
        context = context.clone();
        context.addComponent(new ContextComponent(token.getClass(), token));
        return context;
    }
}
