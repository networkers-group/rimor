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
import java.util.Collections;
import java.util.stream.Collectors;

@Getter
public abstract class RimorProvider<T> extends Annotated<RimorProvider<T>> {

    private final TypeToken<T> providedType;

    protected RimorProvider(Class<T> providedType) {
        this(TypeToken.of(providedType));
    }

    protected RimorProvider(TypeToken<T> providedType) {
        super(null, null);
        this.providedType = providedType;
        this.inspectAnnotations();
    }

    public abstract T get(Token<? super T> token, Injector injector, ExecutionContext context);

    public boolean canProvide(Token<? super T> token) {
        return token.getType().isSupertypeOf(this.providedType) && matchesAnnotations(token);
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