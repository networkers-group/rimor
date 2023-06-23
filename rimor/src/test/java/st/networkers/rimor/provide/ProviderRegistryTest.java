package st.networkers.rimor.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.BarAnnotation;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.annotated.RequireAnnotationTypes;
import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.inject.Token;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProviderRegistryTest {

    static ProviderRegistry providerRegistry = new ProviderRegistry();

    static RimorProvider<String> simpleProvider = new AbstractRimorProvider<String>(String.class) {
        @Override
        public String get(Token<String> token, ExecutionContext context) {
            return "test";
        }
    };

    static RimorProvider<String> annotatedProvider = new AbstractRimorProvider<String>(String.class) {
        @Override
        public String get(Token<String> token, ExecutionContext context) {
            return "foo";
        }
    }.annotatedWith(BarAnnotation.class);

    static RimorProvider<String> annotationRequiredProvider = new AbstractRimorProvider<String>(String.class) {
        @Override
        @RequireAnnotationTypes(FooAnnotation.class)
        public String get(Token<String> token, ExecutionContext context) {
            return "bar";
        }
    };

    @BeforeAll
    static void setUp() {
        providerRegistry.register(simpleProvider);
        providerRegistry.register(annotatedProvider);
        providerRegistry.register(annotationRequiredProvider);
    }

    @Test
    void givenAStringToken_whenFindingProvider_findsSimpleProvider() {
        assertEquals(
                simpleProvider,
                providerRegistry.findFor(new Token<>(String.class), null).orElse(null)
        );
    }

    @Test
    void givenAStringSuperclassToken_whenFindingProvider_findsSimpleProvider() {
        assertEquals(
                simpleProvider,
                providerRegistry.findFor(new Token<>(CharSequence.class), null).orElse(null)
        );
    }

    @Test
    void givenAStringTokenAnnotatedWithBarAnnotation_whenFindingProvider_findsAnnotatedProvider() {
        assertEquals(
                annotatedProvider,
                providerRegistry.findFor(
                        new Token<>(String.class).annotatedWith(BarAnnotation.class), null
                ).orElse(null)
        );
    }

    @Test
    void givenAStringTokenAnnotatedWithFooAnnotation_whenFindingProvider_findsAnnotationRequiredProvider() {
        assertEquals(
                annotationRequiredProvider,
                providerRegistry.findFor(
                        new Token<>(String.class).annotatedWith(new FooAnnotationImpl("bar")), null
                ).orElse(null)
        );
    }
}
