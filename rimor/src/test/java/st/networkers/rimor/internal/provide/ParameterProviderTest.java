package st.networkers.rimor.internal.provide;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.command.parameter.Param;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.reflect.CachedParameter;
import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParameterProviderTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    private @interface Annotation {
    }

    public static class Providers {
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

    public void test(int i, @Deprecated int j, @Param(1) int k) {
    }

    private static final ExecutionContext context = ExecutionContext.build(Collections.emptyList());
    private static final ParameterProviderRegistry providerRegistry = new ParameterProviderRegistry();

    private static CachedParameter intParameter;
    private static CachedParameter annotatedIntParameter;
    private static CachedParameter annotationClassIntParameter;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        providerRegistry.register(new Providers());

        Method method = ParameterProviderTest.class.getMethod("test", int.class, int.class, int.class);

        intParameter = CachedParameter.build(method.getParameters()[0]);
        annotatedIntParameter = CachedParameter.build(method.getParameters()[1]);
        annotationClassIntParameter = CachedParameter.build(method.getParameters()[2]);
    }

    @Test
    void testSimpleProvider() {
        assertEquals(
                -1,
                providerRegistry.findFor(intParameter)
                        .map(parameterProvider -> parameterProvider.get(intParameter, context))
                        .orElse(null)
        );
    }

    @Test
    void testAnnotatedProvider() {
        assertEquals(
                0,
                providerRegistry.findFor(annotatedIntParameter)
                        .map(parameterProvider -> parameterProvider.get(annotatedIntParameter, context))
                        .orElse(null)
        );
    }

    @Test
    void testAnnotationClassProvider() {
        assertEquals(
                1,
                providerRegistry.findFor(annotationClassIntParameter)
                        .map(parameterProvider -> parameterProvider.get(annotationClassIntParameter, context))
                        .orElse(null)
        );
    }
}