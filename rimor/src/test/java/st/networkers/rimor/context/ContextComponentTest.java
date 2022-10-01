package st.networkers.rimor.context;

import org.junit.jupiter.api.Test;
import st.networkers.rimor.ParamImpl;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.builtin.Param;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContextComponentTest {

    static Token stringToken = new Token(String.class);
    static Token intToken = new Token(int.class);
    static Token annotatedStringToken = new Token(String.class).withAnnotation(new ParamImpl(0));

    @Test
    void testSimpleComponent() {
        ContextComponent component = new ContextComponent(String.class, "test");

        assertTrue(component.canProvide(stringToken));
        assertFalse(component.canProvide(intToken));
        assertFalse(component.canProvide(annotatedStringToken));
    }

    @Test
    void testComponentWithAnnotationClass() {
        ContextComponent component = new ContextComponent(Param.class, String.class, "test");

        assertFalse(component.canProvide(stringToken));
        assertFalse(component.canProvide(intToken));
        assertTrue(component.canProvide(annotatedStringToken));
    }

    @Test
    void testComponentWithEqualAnnotation() {
        ContextComponent component = new ContextComponent(new ParamImpl(0), String.class, "test");

        assertFalse(component.canProvide(stringToken));
        assertFalse(component.canProvide(intToken));
        assertTrue(component.canProvide(annotatedStringToken));
    }

    @Test
    void testComponentWithNotEqualAnnotation() {
        ContextComponent component = new ContextComponent(new ParamImpl(1), String.class, "test");

        assertFalse(component.canProvide(stringToken));
        assertFalse(component.canProvide(intToken));
        assertFalse(component.canProvide(annotatedStringToken));
    }
}
