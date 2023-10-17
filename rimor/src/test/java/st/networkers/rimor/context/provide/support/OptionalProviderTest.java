package st.networkers.rimor.context.provide.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.ExecutionContextServiceImpl;
import st.networkers.rimor.context.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockitoSettings
class OptionalProviderTest {

    @Mock ExecutionContextServiceImpl executionContextService;
    @InjectMocks OptionalProvider provider;

    @BeforeEach
    void setUp() {
        when(executionContextService.get(any(), any())).thenCallRealMethod();
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
    void givenContextWithListOfStringsComponent_whenGettingListOfStringsWrappedInOptional_optionalContainsGivenListOfStrings() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(new Token<List<String>>() {}, Arrays.asList("foo", "bar"))
                .build();

        Token<Optional<List<String>>> token = new Token<Optional<List<String>>>() {};
        assertThat(get(token, context)).contains(Arrays.asList("foo", "bar"));
    }

    @Test
    void givenContextWithListOfStringsComponent_whenGettingListOfIntegersWrappedInOptional_optionalIsEmpty() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(new Token<List<String>>() {}, Arrays.asList("foo", "bar"))
                .build();

        Token<Optional<List<Integer>>> token = new Token<Optional<List<Integer>>>() {};
        assertThat(get(token, context)).isEmpty();
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
