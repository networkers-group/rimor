package st.networkers.rimor.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.annotated.RequireAnnotationTypes;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderRegistryTest {

    static ProviderRegistry providerRegistry = new ProviderRegistry();

    static RimorProvider<String> simpleProvider = new AbstractRimorProvider<String>(String.class) {
        @Override
        public String get(Token<String> token, ExecutionContext context) {
            return "test";
        }
    };

    static RimorProvider<String> anyFooAnnotationProvider = new AbstractRimorProvider<String>(String.class) {
        @Override
        @RequireAnnotationTypes(FooAnnotation.class)
        public String get(Token<String> token, ExecutionContext context) {
            return "foo";
        }
    };

    static RimorProvider<Integer> fooAnnotationWithValueSetToBarProvider = new AbstractRimorProvider<Integer>(int.class) {
        @Override
        @FooAnnotation("bar")
        public Integer get(Token<Integer> token, ExecutionContext context) {
            return Integer.MAX_VALUE;
        }
    };

    @BeforeAll
    static void setUp() {
        providerRegistry.register(simpleProvider);
        providerRegistry.register(anyFooAnnotationProvider);
        providerRegistry.register(fooAnnotationWithValueSetToBarProvider);
    }

    @Test
    void givenAStringToken_whenFindingProvider_findsSimpleProvider() {
        Token<String> token = Token.of(String.class);
        assertThat(providerRegistry.findFor(token)).containsInstanceOf(simpleProvider.getClass());
    }

    @Test
    void givenAStringSuperclassToken_whenFindingProvider_findsSuitableProvider() {
        Token<CharSequence> token = Token.of(CharSequence.class).annotatedWith(FooAnnotation.class);
        assertThat(providerRegistry.findFor(token)).containsInstanceOf(anyFooAnnotationProvider.getClass());
    }

    @Test
    void givenATokenWithATypeWithNoSuitableProviders_whenFindingProvider_findsNothing() {
        Token<Integer> token = Token.of(int.class);
        assertThat(providerRegistry.findFor(token)).isEmpty();
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token<String> token = Token.of(String.class).annotatedWith(FooAnnotation.class);
        assertThat(providerRegistry.findFor(token)).containsInstanceOf(anyFooAnnotationProvider.getClass());
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotationImplementation_whenFindingProvider_findsAnyFooAnnotationProvider() {
        Token<String> token = Token.of(String.class).annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(providerRegistry.findFor(token)).containsInstanceOf(anyFooAnnotationProvider.getClass());
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotationWithValueSetToBar_whenFindingProvider_findsFooAnnotationWithValueSetToBarProvider() {
        Token<Integer> token = Token.of(int.class).annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(providerRegistry.findFor(token)).containsInstanceOf(fooAnnotationWithValueSetToBarProvider.getClass());
    }

    @Test
    void givenAnIntTokenAnnotatedWithFooAnnotationWithValueSetToBar_whenFindingProvider_findsFooAnnotationWithValueSetToBarProvider() {
        Token<Integer> token = Token.of(int.class).annotatedWith(new FooAnnotationImpl("bar"));
        assertThat(providerRegistry.findFor(token)).containsInstanceOf(fooAnnotationWithValueSetToBarProvider.getClass());
    }

    @Test
    void givenAnIntToken_whenFindingProvider_findsNothing() {
        Token<Integer> token = Token.of(int.class);
        assertThat(providerRegistry.findFor(token)).isEmpty();
    }
}
