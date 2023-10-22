package st.networkers.rimor.context.provide;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.qualify.RequireQualifiers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class ExecutionContextProviderRegistryTest {

    static ExecutionContextProviderRegistry registry;

    static class SimpleProvider extends AbstractExecutionContextProvider<String> {
        public SimpleProvider() {
            super(String.class);
        }

        @Override
        public String get(Token token, ExecutionContext context) {
            return "test";
        }
    }

    @RequireQualifiers(FooAnnotation.class)
    static class AnyFooAnnotationProvider extends AbstractExecutionContextProvider<String> {
        public AnyFooAnnotationProvider() {
            super(String.class);
        }

        @Override
        public String get(Token token, ExecutionContext context) {
            return "foo";
        }
    }

    @FooAnnotation("bar")
    static class FooAnnotationWithValueSetToBarStringProvider extends AbstractExecutionContextProvider<String> {
        public FooAnnotationWithValueSetToBarStringProvider() {
            super(String.class);
        }

        @Override
        public String get(Token token, ExecutionContext context) {
            return "bar";
        }
    }

    @FooAnnotation("bar")
    static class FooAnnotationWithValueSetToBarIntProvider extends AbstractExecutionContextProvider<Integer> {
        public FooAnnotationWithValueSetToBarIntProvider() {
            super(int.class);
        }

        @Override
        public Integer get(Token token, ExecutionContext context) {
            return Integer.MAX_VALUE;
        }
    }

    @BeforeEach
    void setUp() {
        registry = new ExecutionContextProviderRegistry();

        registry.register(new SimpleProvider());
        registry.register(new AnyFooAnnotationProvider());
        registry.register(new FooAnnotationWithValueSetToBarIntProvider());
    }

    @Test
    void givenAProviderWithTokenMatchingAnAlreadyRegisteredProvider_whenRegistering_throwsIllegalArgumentException() {
        assertThatCode(() -> registry.register(new FooAnnotationWithValueSetToBarStringProvider()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("found several execution context providers with matching tokens");
    }

    // SimpleProvider

    @Test
    void givenAStringToken_whenFindingProvider_findsSimpleProvider() {
        Token token = Token.of(String.class);
        assertThat(registry.findFor(token)).containsInstanceOf(SimpleProvider.class);
    }

    @Test
    void givenAStringSuperclassToken_whenFindingProvider_findsSimpleProvider() {
        Token token = Token.of(CharSequence.class);

        // CharSequence is not directly mapped to any provider, ExecutionContextProviderRegistry#findAnySuitable should do its job
        assertThat(registry.findFor(token)).containsInstanceOf(SimpleProvider.class);
    }

    // AnyFooAnnotationProvider

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token token = Token.of(String.class).qualifiedWith(FooAnnotation.class);
        assertThat(registry.findFor(token)).containsInstanceOf(AnyFooAnnotationProvider.class);
    }

    @Test
    void givenAStringSuperclassTokenAnnotatedWithFooAnnotation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token token = Token.of(CharSequence.class).qualifiedWith(FooAnnotation.class);

        // CharSequence is not directly mapped to any provider, ExecutionContextProviderRegistry#findAnySuitable should do its job
        assertThat(registry.findFor(token)).containsInstanceOf(AnyFooAnnotationProvider.class);
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotationImplementation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token token = Token.of(String.class).qualifiedWith(new FooAnnotationImpl("bar"));
        assertThat(registry.findFor(token)).containsInstanceOf(AnyFooAnnotationProvider.class);
    }

    @Test
    void givenAStringSuperclassTokenAnnotatedWithFooAnnotationImplementation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token token = Token.of(CharSequence.class).qualifiedWith(new FooAnnotationImpl("bar"));

        // CharSequence is not directly mapped to any provider, ExecutionContextProviderRegistry#findAnySuitable should do its job
        assertThat(registry.findFor(token)).containsInstanceOf(AnyFooAnnotationProvider.class);
    }

    // FooAnnotationWithValueSetToBarIntProvider

    @Test
    void givenAnIntTokenAnnotatedWithFooAnnotationWithValueSetToBar_whenFindingProvider_findsFooAnnotationWithValueSetToBarIntProvider() {
        Token token = Token.of(int.class).qualifiedWith(new FooAnnotationImpl("bar"));
        assertThat(registry.findFor(token)).containsInstanceOf(FooAnnotationWithValueSetToBarIntProvider.class);
    }

    @Test
    void givenAnIntSuperclassTokenAnnotatedWithFooAnnotationWithValueSetToBar_whenFindingProvider_findsFooAnnotationWithValueSetToBarIntProvider() {
        Token token = Token.of(Number.class).qualifiedWith(new FooAnnotationImpl("bar"));

        // Number is not directly mapped to any provider, ExecutionContextProviderRegistry#findAnySuitable should do its job
        assertThat(registry.findFor(token)).containsInstanceOf(FooAnnotationWithValueSetToBarIntProvider.class);
    }

    // No matching providers

    @Test
    void givenAnIntTokenAnnotatedWithFooAnnotationWithValueSetToBaz_whenFindingProvider_findsNothing() {
        Token token = Token.of(int.class).qualifiedWith(new FooAnnotationImpl("baz"));
        assertThat(registry.findFor(token)).isEmpty();
    }

    @Test
    void givenAnIntToken_whenFindingProvider_findsNothing() {
        Token token = Token.of(int.class);
        assertThat(registry.findFor(token)).isEmpty();
    }
}
