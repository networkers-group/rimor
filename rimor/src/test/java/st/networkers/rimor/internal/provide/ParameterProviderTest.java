package st.networkers.rimor.internal.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.reflect.CachedMethod;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ParameterProviderWrapper;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.provide.builtin.Param;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ParameterProviderTest {

    public static class TestProviders implements ParameterProviderWrapper {
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
        public int provideAnnotatedInt(CachedParameter parameter) {
            return parameter.getAnnotation(Param.class).value();
        }

        // does not have the ProvidesParameter annotation
        public String notAProvider() {
            return String.class.getName();
        }
    }

    public void injectableMethod(int i, @Deprecated int j, @Param(1) int k, String l) {
    }

    private static Object[] provided;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        Injector injector = new Injector().registerParameterProviders(new TestProviders());
        Method method = ParameterProviderTest.class.getMethod("injectableMethod", int.class, int.class, int.class, String.class);

        provided = injector.resolveParameters(CachedMethod.build(method), ExecutionContext.build(Collections.emptyList()));
    }

    @Test
    void testSimpleProvider() {
        assertEquals(-1, provided[0]);
    }

    @Test
    void testAnnotatedProvider() {
        assertEquals(0, provided[1]);
    }

    @Test
    void testAnnotationClassProvider() {
        assertEquals(1, provided[2]);
    }

    @Test
    void testUnboundProvider() {
        assertNull(provided[3]);
    }
}
