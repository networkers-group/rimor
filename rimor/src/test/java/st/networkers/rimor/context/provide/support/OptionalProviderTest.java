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
import st.networkers.rimor.qualify.ParameterizedToken;
import st.networkers.rimor.qualify.Token;

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

        Token token = new ParameterizedToken<Optional<String>>() {};
        assertThat(provider.get(token, context)).contains("foo");
    }

    @Test
    void givenContextWithStringAnnotatedWithFooAnnotation_whenGettingStringWrappedInOptionalAnnotatedWithFooAnnotation_optionalContainsGivenString() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(Token.of(String.class).qualifiedWith(FooAnnotation.class), "foo")
                .build();

        Token token = new ParameterizedToken<Optional<String>>() {}.qualifiedWith(new FooAnnotationImpl("bar"));
        assertThat(provider.get(token, context)).contains("foo");
    }

    @Test
    void givenContextWithIntComponent_whenGettingIntegerWrappedInOptional_optionalContainsGivenInteger() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(int.class, 3)
                .build();

        Token token = new ParameterizedToken<Optional<Integer>>() {};
        assertThat(provider.get(token, context)).contains(3);
    }

    @Test
    void givenContextWithListOfStringsComponent_whenGettingListOfStringsWrappedInOptional_optionalContainsGivenListOfStrings() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(new ParameterizedToken<List<String>>() {}, Arrays.asList("foo", "bar"))
                .build();

        Token token = new ParameterizedToken<Optional<List<String>>>() {};
        assertThat(provider.get(token, context)).contains(Arrays.asList("foo", "bar"));
    }

    @Test
    void givenContextWithListOfStringsComponent_whenGettingListOfIntegersWrappedInOptional_optionalIsEmpty() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(new ParameterizedToken<List<String>>() {}, Arrays.asList("foo", "bar"))
                .build();

        Token token = new ParameterizedToken<Optional<List<Integer>>>() {};
        assertThat(provider.get(token, context)).isEmpty();
    }

    @Test
    void givenContextWithIntComponent_whenGettingStringWrappedInOptional_optionalIsEmpty() {
        ExecutionContext context = ExecutionContext.builder()
                .bind(int.class, 3)
                .build();

        Token token = new ParameterizedToken<Optional<String>>() {};
        assertThat(provider.get(token, context)).isEmpty();
    }
}
