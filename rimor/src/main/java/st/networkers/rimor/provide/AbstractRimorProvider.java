package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.AbstractAnnotated;
import st.networkers.rimor.util.InspectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public abstract class AbstractRimorProvider<T>
        extends AbstractAnnotated<AbstractRimorProvider<T>>
        implements RimorProvider<T>
{
    private final Collection<TypeToken<T>> providedTypes;

    @SafeVarargs
    protected AbstractRimorProvider(Class<T>... providedTypes) {
        this(Arrays.stream(providedTypes).map(TypeToken::of).collect(Collectors.toList()));
    }

    @SafeVarargs
    protected AbstractRimorProvider(TypeToken<T>... providedTypes) {
        this(Arrays.asList(providedTypes));
    }

    protected AbstractRimorProvider(Collection<TypeToken<T>> providedTypes) {
        this.providedTypes = providedTypes;
        this.inspectAnnotations();
    }

    @Override
    public Collection<TypeToken<T>> getProvidedTypes() {
        return providedTypes;
    }

    @Override
    public boolean canProvide(Token<?> token) {
        for (TypeToken<T> providedType : this.providedTypes)
            if (token.getType().isSupertypeOf(providedType))
                return matchesAnnotations(token) && token.matchesAnnotations(this);
        return false;
    }

    private void inspectAnnotations() {
        Method method;
        try {
            method = this.getClass().getMethod("get", Token.class, Injector.class, ExecutionContext.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        this.annotations.putAll(InspectionUtils.getMappedAnnotations(
                Arrays.stream(method.getAnnotations())
                        .filter(annotation -> !(annotation instanceof RequireAnnotations))
                        .collect(Collectors.toList())
        ));

        if (method.isAnnotationPresent(RequireAnnotations.class))
            Collections.addAll(this.requiredAnnotations, method.getAnnotation(RequireAnnotations.class).value());
    }
}
