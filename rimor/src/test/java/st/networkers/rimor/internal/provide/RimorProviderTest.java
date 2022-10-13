package st.networkers.rimor.internal.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.ParamImpl;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.provide.RimorProvider;
import st.networkers.rimor.provide.builtin.Param;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RimorProviderTest {

    static Injector injector;
    static ExecutionContext context = ExecutionContext.build(
            new ContextComponent<>(double.class, 0d)
    );

    @BeforeAll
    static void setup() {
        // simple int, returns -1
        RimorProvider<Integer> simpleProvider = new RimorProvider<Integer>(int.class) {
            @Override
            public Integer get(Token<? super Integer> token, Injector injector, ExecutionContext context) {
                return -1;
            }
        };

        // int annotated with @Deprecated, returns what the injector provides for a simple double token
        RimorProvider<Integer> annotatedProvider = new RimorProvider<Integer>(int.class) {
            @Override
            @Deprecated
            public Integer get(Token<? super Integer> token, Injector injector, ExecutionContext context) {
                return injector.get(new Token<>(double.class), context).intValue();
            }
        };

        // int annotated with @Param, returns what its Param#value indicates
        RimorProvider<Integer> annotationRequiredProvider = new RimorProvider<Integer>(int.class) {
            @Override
            @RequireAnnotations(Param.class)
            public Integer get(Token<? super Integer> token, Injector injector, ExecutionContext context) {
                return token.getAnnotation(Param.class).value();
            }
        };

        injector = new Injector().registerProviders(simpleProvider, annotatedProvider, annotationRequiredProvider);
    }

    @Test
    void testSimpleProvider() {
        assertEquals(-1, injector.get(
                new Token<>(int.class),
                context
        ));
    }

    @Test
    void testAnnotatedProvider() {
        assertEquals(0, injector.get(
                new Token<>(int.class).annotatedWith(Deprecated.class),
                context
        ));
    }

    @Test
    void testAnnotationRequiredProvider() {
        assertEquals(1, injector.get(
                new Token<>(int.class).annotatedWith(new ParamImpl(1)),
                context
        ));
    }

    @Test
    void testUnboundProvider() {
        assertNull(injector.get(
                new Token<>(String.class),
                context
        ));
    }
}
