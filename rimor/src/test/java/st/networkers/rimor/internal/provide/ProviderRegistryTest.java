package st.networkers.rimor.internal.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.ParamImpl;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Injector;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.ProviderRegistry;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.provide.RimorProvider;
import st.networkers.rimor.provide.builtin.Param;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProviderRegistryTest {

    static ProviderRegistry providerRegistry = new ProviderRegistryImpl();

    static RimorProvider<String> simpleProvider = new RimorProvider<String>(String.class) {
        @Override
        public String get(Token<String> token, Injector injector, ExecutionContext context) {
            return "test";
        }
    };

    static RimorProvider<String> annotatedProvider = new RimorProvider<String>(String.class) {
        @Override
        public String get(Token<String> token, Injector injector, ExecutionContext context) {
            return "foo";
        }
    }.annotatedWith(Deprecated.class);

    static RimorProvider<String> annotationRequiredProvider = new RimorProvider<String>(String.class) {
        @Override
        @RequireAnnotations(Param.class)
        public String get(Token<String> token, Injector injector, ExecutionContext context) {
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
    void givenAStringToken_whenFindingProvider_thenSimpleProvider() {
        assertEquals(
                simpleProvider,
                providerRegistry.findFor(new Token<>(String.class)).orElse(null)
        );
    }

    @Test
    void givenAStringSuperclassToken_whenFindingProvider_thenSimpleProvider() {
        assertEquals(
                simpleProvider,
                providerRegistry.findFor(new Token<>(CharSequence.class)).orElse(null)
        );
    }

    @Test
    void givenAStringTokenAnnotatedWithDeprecated_whenFindingProvider_thenAnnotatedProvider() {
        assertEquals(
                annotatedProvider,
                providerRegistry.findFor(
                        new Token<>(String.class).annotatedWith(Deprecated.class)
                ).orElse(null)
        );
    }

    @Test
    void givenAStringTokenAnnotatedWithParam_whenFindingProvider_thenAnnotationRequiredProvider() {
        assertEquals(
                annotationRequiredProvider,
                providerRegistry.findFor(
                        new Token<>(String.class).annotatedWith(new ParamImpl(0))
                ).orElse(null)
        );
    }
}
