package st.networkers.rimor.internal.provide.builtin;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.BarAnnotation;
import st.networkers.rimor.BarAnnotationImpl;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.InjectorImpl;
import st.networkers.rimor.internal.provide.ProviderRegistryImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionalProviderTest {

    static OptionalProvider provider = new OptionalProvider();
    static Injector injector = new InjectorImpl(new ProviderRegistryImpl());

    @Test
    void givenContextWithFooComponent_whenGettingFooWrappedInOptional_thenOptionalWrapsFoo() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(String.class, "foo")
        );

        Token<Optional<?>> token = buildToken(new TypeToken<Optional<String>>() {});
        assertEquals(provider.get(token, injector, context), Optional.of("foo"));
    }

    @Test
    void givenContextWithFooComponentAnnotatedWithBar_whenGettingFooWrappedInOptionalAnnotatedWithBar_thenOptionalWrapsFoo() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(String.class, "foo").annotatedWith(new BarAnnotationImpl(0))
        );

        Token<Optional<?>> token = buildToken(new TypeToken<Optional<String>>() {})
                .annotatedWith(BarAnnotation.class);
        assertEquals(provider.get(token, injector, context), Optional.of("foo"));
    }

    @SuppressWarnings("unchecked")
    Token<Optional<?>> buildToken(TypeToken<? extends Optional<?>> type) {
        return (Token<Optional<?>>) new Token<>(type);
    }
}