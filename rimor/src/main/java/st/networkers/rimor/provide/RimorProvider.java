package st.networkers.rimor.provide;

import com.google.common.reflect.TypeToken;
import lombok.Getter;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Annotated;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.util.InspectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Getter
public abstract class RimorProvider<T> extends Annotated<RimorProvider<T>> {

    private final Collection<TypeToken<T>> providedTypes;

    @SafeVarargs
    protected RimorProvider(Class<T>... providedTypes) {
        this(Arrays.stream(providedTypes).map(TypeToken::of).collect(Collectors.toList()));
    }

    @SafeVarargs
    protected RimorProvider(TypeToken<T>... providedTypes) {
        this(Arrays.asList(providedTypes));
    }

    protected RimorProvider(Collection<TypeToken<T>> providedTypes) {
        super(null, null);
        this.providedTypes = providedTypes;
        this.inspectAnnotations();
    }

    public abstract T get(Token<T> token, Injector injector, ExecutionContext context);

    public boolean canProvide(Token<?> token) {
        for (TypeToken<T> providedType : this.providedTypes)
            if (token.getType().isSupertypeOf(providedType))
                return matchesAnnotations(token);
        return false;
    }

    private void inspectAnnotations() {
        try {
            Method method = this.getClass().getMethod("get", Token.class, Injector.class, ExecutionContext.class);
            this.annotations = InspectionUtils.getMappedAnnotations(Arrays.stream(method.getAnnotations())
                    .filter(annotation -> !(annotation instanceof RequireAnnotations))
                    .collect(Collectors.toList()));
            this.requiredAnnotations = method.isAnnotationPresent(RequireAnnotations.class)
                    ? Arrays.asList(method.getAnnotation(RequireAnnotations.class).value())
                    : Collections.emptyList();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}