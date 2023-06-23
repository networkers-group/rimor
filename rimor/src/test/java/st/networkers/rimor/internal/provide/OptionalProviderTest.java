package st.networkers.rimor.internal.provide;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.inject.ContextComponent;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.internal.inject.RimorInjectorImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OptionalProviderTest {

    static OptionalProvider provider;

    @BeforeAll
    static void setUp() {
        provider = new OptionalProvider(new RimorInjectorImpl());
    }

    @Test
    void givenContextWithStringComponent_whenGettingStringWrappedInOptional_optionalContainsGivenString() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(String.class, "foo")
        );

        TypeToken<Optional<String>> type = new TypeToken<Optional<String>>() {};
        assertThat(provider.get(buildToken(type), context)).isEqualTo(Optional.of("foo"));
    }

    @Test
    void givenContextWithStringAnnotatedWithFooAnnotation_whenGettingOptionalStringAnnotatedWithFooAnnotation_optionalContainsGivenString() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(String.class, "foo").annotatedWith(new FooAnnotationImpl("bar"))
        );

        Token<Optional<?>> token = buildToken(new TypeToken<Optional<String>>() {})
                .annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(provider.get(token, context)).isEqualTo(Optional.of("foo"));
    }

    @Test
    void givenContextWithIntComponent_whenGettingIntegerWrappedInOptional_optionalContainsGivenInteger() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(int.class, 3)
        );

        TypeToken<Optional<Integer>> type = new TypeToken<Optional<Integer>>() {};
        assertThat(provider.get(buildToken(type), context)).isEqualTo(Optional.of(3));
    }

    @Test
    void givenContextWithIntComponent_whenGettingStringWrappedInOptional_optionalIsEmpty() {
        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(int.class, 3)
        );

        TypeToken<Optional<String>> type = new TypeToken<Optional<String>>() {};
        assertThat(provider.get(buildToken(type), context)).isEmpty();
    }

    @SuppressWarnings("unchecked")
    Token<Optional<?>> buildToken(TypeToken<? extends Optional<?>> type) {
        return (Token<Optional<?>>) new Token<>(type);
    }
}
