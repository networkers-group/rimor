package st.networkers.rimor.inject.provide.support;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.inject.RimorInjectorImpl;

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
        ExecutionContext context = ExecutionContext.builder()
                .bind(String.class, "foo")
                .build();

        Token<Optional<String>> token = new Token<Optional<String>>() {};
        assertThat(get(token, context)).contains("foo");
    }

    @Test
    void givenContextWithStringAnnotatedWithFooAnnotation_whenGettingStringWrappedInOptionalAnnotatedWithFooAnnotation_optionalContainsGivenString() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(Token.of(String.class).annotatedWith(FooAnnotation.class), "foo")
                .build();

        Token<Optional<String>> token = new Token<Optional<String>>() {}.annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(get(token, context)).contains("foo");
    }

    @Test
    void givenContextWithIntComponent_whenGettingIntegerWrappedInOptional_optionalContainsGivenInteger() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(int.class, 3)
                .build();

        Token<Optional<Integer>> token = new Token<Optional<Integer>>() {};
        assertThat(get(token, context)).contains(3);
    }

    @Test
    void givenContextWithIntComponent_whenGettingStringWrappedInOptional_optionalIsEmpty() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(int.class, 3)
                .build();

        Token<Optional<String>> token = new Token<Optional<String>>() {};
        assertThat(get(token, context)).isEmpty();
    }

    @SuppressWarnings("unchecked")
    Optional<Object> get(Token<? extends Optional<?>> token, ExecutionContext context) {
        return (Optional<Object>) provider.get((Token<Optional<?>>) token, context);
    }
}
