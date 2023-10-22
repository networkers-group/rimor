package st.networkers.rimor.context.provide;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.qualify.ImmutableToken;
import st.networkers.rimor.qualify.Qualified;
import st.networkers.rimor.qualify.Token;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@MockitoSettings
class HandlerMethodExecutionContextProviderTest {

    @Test
    void givenPrimitivesInProvidedTypes_whenBuilding_primitivesAreWrapped() {
        HandlerMethodExecutionContextProvider executionContextProvider = HandlerMethodExecutionContextProvider.build(
                null, null, null, Arrays.asList(int.class, boolean.class));

        assertThat(executionContextProvider.getProvidedTypes()).containsExactlyInAnyOrder(Integer.class, Boolean.class);
    }

    @Mock ExecutionContextService executionContextService;
    @Captor ArgumentCaptor<ExecutionContext> executionContextCaptor;

    @Test
    void givenExecutionContext_whenUsingProvider_invokesMethodWithCopyOfTheExecutionContext() {
        HandlerMethodExecutionContextProvider executionContextProvider = HandlerMethodExecutionContextProvider.build(
                executionContextService, null, null, Collections.singletonList(int.class));

        ExecutionContext executionContext = ExecutionContext.builder()
                .bind(String.class, "foo")
                .build();

        executionContextProvider.get(Token.of(int.class), executionContext);
        verify(executionContextService).invokeMethod(any(), any(), executionContextCaptor.capture());

        ExecutionContext executionContextCopy = executionContextCaptor.getValue();
        assertThat(executionContextCopy.get(Token.of(String.class))).contains("foo");
    }

    @Test
    void whenUsingProvider_executionContextContainsImmutableCopyOfGivenToken() {
        HandlerMethodExecutionContextProvider executionContextProvider = HandlerMethodExecutionContextProvider.build(
                executionContextService, null, null, Collections.singletonList(int.class));

        Token givenToken = Token.of(int.class);

        executionContextProvider.get(givenToken, ExecutionContext.builder().build());
        verify(executionContextService).invokeMethod(any(), any(), executionContextCaptor.capture());

        assertThat(executionContextCaptor.getValue().<Token>get(Token.of(Token.class)))
                .containsInstanceOf(ImmutableToken.class)
                .hasValueSatisfying(immutableToken -> {
                    assertThat(immutableToken.getType()).isEqualTo(givenToken.getType());
                    assertThat(immutableToken.containsAllQualifiersOf(givenToken, Qualified.AssignCriteria.EQUALS)).isTrue();
                });
    }

    @Test
    void givenTokenWithAnnotations_whenUsingProvider_executionContextContainsGivenTokenAnnotations() {
        HandlerMethodExecutionContextProvider executionContextProvider = HandlerMethodExecutionContextProvider.build(
                executionContextService, null, null, Collections.singletonList(int.class));

        Token givenToken = Token.of(int.class)
                .qualifiedWith(new FooAnnotationImpl("foo"));

        executionContextProvider.get(givenToken, ExecutionContext.builder().build());
        verify(executionContextService).invokeMethod(any(), any(), executionContextCaptor.capture());

        assertThat(executionContextCaptor.getValue().<FooAnnotation>get(Token.of(FooAnnotation.class)))
                .contains(new FooAnnotationImpl("foo"));
    }
}
