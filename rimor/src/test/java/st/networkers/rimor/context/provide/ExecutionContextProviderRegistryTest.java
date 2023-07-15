package st.networkers.rimor.context.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.annotation.RequireQualifiers;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;

import static org.assertj.core.api.Assertions.assertThat;

class ExecutionContextProviderRegistryTest {

    static ExecutionContextProviderRegistry executionContextProviderRegistry = new ExecutionContextProviderRegistry();

    static ExecutionContextProvider<String> simpleProvider = new AbstractExecutionContextProvider<String>(String.class) {
        @Override
        public String get(Token<String> token, ExecutionContext context) {
            return "test";
        }
    };

    static ExecutionContextProvider<String> anyFooAnnotationProvider = new AbstractExecutionContextProvider<String>(String.class) {
        @Override
        @RequireQualifiers(FooAnnotation.class)
        public String get(Token<String> token, ExecutionContext context) {
            return "foo";
        }
    };

    static ExecutionContextProvider<Integer> fooAnnotationWithValueSetToBarProvider = new AbstractExecutionContextProvider<Integer>(int.class) {
        @Override
        @FooAnnotation("bar")
        public Integer get(Token<Integer> token, ExecutionContext context) {
            return Integer.MAX_VALUE;
        }
    };

    @BeforeAll
    static void setUp() {
        executionContextProviderRegistry.register(simpleProvider);
        executionContextProviderRegistry.register(anyFooAnnotationProvider);
        executionContextProviderRegistry.register(fooAnnotationWithValueSetToBarProvider);
    }

    @Test
    void givenAStringToken_whenFindingProvider_findsSimpleProvider() {
        Token<String> token = Token.of(String.class);
        assertThat(executionContextProviderRegistry.findFor(token)).containsInstanceOf(simpleProvider.getClass());
    }

    @Test
    void givenAStringSuperclassToken_whenFindingProvider_findsSuitableProvider() {
        Token<CharSequence> token = Token.of(CharSequence.class).annotatedWith(FooAnnotation.class);
        assertThat(executionContextProviderRegistry.findFor(token)).containsInstanceOf(anyFooAnnotationProvider.getClass());
    }

    @Test
    void givenATokenWithATypeWithNoSuitableProviders_whenFindingProvider_findsNothing() {
        Token<Integer> token = Token.of(int.class);
        assertThat(executionContextProviderRegistry.findFor(token)).isEmpty();
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token<String> token = Token.of(String.class).annotatedWith(FooAnnotation.class);
        assertThat(executionContextProviderRegistry.findFor(token)).containsInstanceOf(anyFooAnnotationProvider.getClass());
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotationImplementation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token<String> token = Token.of(String.class).annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(executionContextProviderRegistry.findFor(token)).containsInstanceOf(anyFooAnnotationProvider.getClass());
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotationWithValueSetToBar_whenFindingProvider_findsFooAnnotationWithValueSetToBarProvider() {
        Token<Integer> token = Token.of(int.class).annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(executionContextProviderRegistry.findFor(token)).containsInstanceOf(fooAnnotationWithValueSetToBarProvider.getClass());
    }

    @Test
    void givenAnIntTokenAnnotatedWithFooAnnotationWithValueSetToBar_whenFindingProvider_findsFooAnnotationWithValueSetToBarProvider() {
        Token<Integer> token = Token.of(int.class).annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(executionContextProviderRegistry.findFor(token)).containsInstanceOf(fooAnnotationWithValueSetToBarProvider.getClass());
    }

    @Test
    void givenAnIntToken_whenFindingProvider_findsNothing() {
        Token<Integer> token = Token.of(int.class);
        assertThat(executionContextProviderRegistry.findFor(token)).isEmpty();
    }
}
