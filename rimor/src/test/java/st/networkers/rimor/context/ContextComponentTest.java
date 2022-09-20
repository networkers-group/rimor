package st.networkers.rimor.context;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.command.parameter.Param;
import st.networkers.rimor.internal.reflect.CachedParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContextComponentTest {

    private static CachedParameter stringParameter;
    private static CachedParameter integerParameter;
    private static CachedParameter paramStringParameter;

    @BeforeAll
    static void setup() throws NoSuchMethodException {
        Method method = ContextComponentTest.class.getMethod("parameters", String.class, Integer.class, String.class);

        stringParameter = CachedParameter.build(method.getParameters()[0]);
        integerParameter = CachedParameter.build(method.getParameters()[1]);
        paramStringParameter = CachedParameter.build(method.getParameters()[2]);
    }

    @Test
    void testSimpleComponent() {
        ContextComponent component = new ContextComponent(String.class, "test");

        assertTrue(component.canProvide(stringParameter));
        assertFalse(component.canProvide(integerParameter));
        assertFalse(component.canProvide(paramStringParameter));
    }

    @Test
    void testComponentWithAnnotationClass() {
        ContextComponent component = new ContextComponent(Param.class, String.class, "test");

        assertFalse(component.canProvide(stringParameter));
        assertFalse(component.canProvide(integerParameter));
        assertTrue(component.canProvide(paramStringParameter));
    }

    @Test
    void testComponentWithEqualAnnotation() {
        ContextComponent component = new ContextComponent(new ParamImpl(0), String.class, "test");

        assertFalse(component.canProvide(stringParameter));
        assertFalse(component.canProvide(integerParameter));
        assertTrue(component.canProvide(paramStringParameter));
    }

    @Test
    void testComponentWithNotEqualAnnotation() {
        ContextComponent component = new ContextComponent(new ParamImpl(1), String.class, "test");

        assertFalse(component.canProvide(stringParameter));
        assertFalse(component.canProvide(integerParameter));
        assertFalse(component.canProvide(paramStringParameter));
    }

    public void parameters(String string, Integer integer, @Param(0) String paramString) {
    }

    private static class ParamImpl implements Param {
        private final int value;

        private ParamImpl(int value) {
            this.value = value;
        }

        @Override
        public int value() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Param)) return false;
            Param param = (Param) o;
            return value == param.value();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Param.class;
        }
    }
}