package st.networkers.rimor.internal.provide.builtin;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.BarAnnotation;
import st.networkers.rimor.BarAnnotationImpl;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.RimorInjectorImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OptionalProviderTest {

    static OptionalProvider provider;

    @BeforeAll
    static void setUp() {
        provider = new OptionalProvider(new RimorInjectorImpl());
    }

    @Test
    void givenContextWithFooStringComponent_whenGettingFooWrappedInOptional_thenOptionalWrapsFoo() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(String.class, "foo")
        );

        Token<Optional<?>> token = buildToken(new TypeToken<Optional<String>>() {});
        assertEquals(provider.get(token, context), Optional.of("foo"));
    }

    @Test
    void givenContextWithFooStringComponentAnnotatedWithBar_whenGettingFooWrappedInOptionalAnnotatedWithBar_thenOptionalWrapsFoo() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(String.class, "foo").annotatedWith(new BarAnnotationImpl(0))
        );

        Token<Optional<?>> token = buildToken(new TypeToken<Optional<String>>() {})
                .annotatedWith(BarAnnotation.class);
        assertEquals(provider.get(token, context), Optional.of("foo"));
    }

    @Test
    void givenContextWithIntComponent_whenGettingIntegerWrappedInOptional_thenOptionalWrapsGivenInteger() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(int.class, 3)
        );

        Token<Optional<?>> token = buildToken(new TypeToken<Optional<Integer>>() {});
        assertEquals(provider.get(token, context), Optional.of(3));
    }

    @Test
    void givenContextWithIntComponent_whenGettingStringWrappedInOptional_thenOptionalIsEmpty() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(int.class, 3)
        );

        Token<Optional<?>> token = buildToken(new TypeToken<Optional<String>>() {});
        assertThat(provider.get(token, context)).isEmpty();
    }

    @SuppressWarnings("unchecked")
    Token<Optional<?>> buildToken(TypeToken<? extends Optional<?>> type) {
        return (Token<Optional<?>>) new Token<>(type);
    }
}
