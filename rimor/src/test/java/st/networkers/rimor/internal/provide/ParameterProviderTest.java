package st.networkers.rimor.internal.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.provide.builtin.Param;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterProviderTest {

    public static class TestProviders {
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
        public int provideAnnotatedInt(Param param) {
            return param.value();
        }
    }

    public void injectableMethod(int i, @Deprecated int j, @Param(1) int k) {
    }

    private static final ExecutionContext context = ExecutionContext.build(Collections.emptyList());
    private static final ParameterProviderRegistry providerRegistry = new ParameterProviderRegistry();

    private static CachedParameter intParameter;
    private static CachedParameter annotatedIntParameter;
    private static CachedParameter annotationClassIntParameter;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        providerRegistry.register(new TestProviders());

        Method method = ParameterProviderTest.class.getMethod("injectableMethod", int.class, int.class, int.class);

        intParameter = CachedParameter.build(method.getParameters()[0]);
        annotatedIntParameter = CachedParameter.build(method.getParameters()[1]);
        annotationClassIntParameter = CachedParameter.build(method.getParameters()[2]);
    }

    @Test
    void testSimpleProvider() {
        assertEquals(-1, provide(intParameter));
    }

    @Test
    void testAnnotatedProvider() {
        assertEquals(0, provide(annotatedIntParameter));
    }

    @Test
    void testAnnotationClassProvider() {
        assertEquals(1, provide(annotationClassIntParameter));
    }

    private Object provide(CachedParameter parameter) {
        return providerRegistry.provide(parameter, context).orElse(null);
    }
}
