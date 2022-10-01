package st.networkers.rimor.internal.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.ParamImpl;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.provide.RimorProviderWrapper;
import st.networkers.rimor.provide.builtin.Param;

import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProviderTest {

    public static class TestProviders implements RimorProviderWrapper {
        @ProvidesParameter
        public int provideInt() {
            return -1;
        }

        @ProvidesParameter
        @Deprecated
        public int provideAnnotatedInt() {
            return 0;
        }

        @ProvidesParameter
        @RequireAnnotations(Param.class)
        public int provideAnnotatedInt(Token token) {
            return token.getAnnotation(Param.class).value();
        }

        // does not have the ProvidesParameter annotation
        public String notAProvider() {
            return String.class.getName();
        }
    }

    static Injector injector;
    static ExecutionContext context = ExecutionContext.build(Collections.emptyList());

    @BeforeAll
    static void setup() {
        injector = new Injector().registerProviders(new TestProviders());
    }

    @Test
    void testSimpleProvider() {
        assertEquals(-1, injector.get(new Token(int.class), context));
    }

    @Test
    void testAnnotatedProvider() {
        assertEquals(0, injector.get(new Token(int.class).withAnnotation(new DeprecatedImpl()), context));
    }

    @Test
    void testAnnotationClassProvider() {
        assertEquals(1, injector.get(new Token(int.class).withAnnotation(new ParamImpl(1)), context));
    }

    @Test
    void testUnboundProvider() {
        assertNull(injector.get(new Token(String.class), context));
    }

    static class DeprecatedImpl implements Deprecated {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Deprecated.class;
        }
    }
}
