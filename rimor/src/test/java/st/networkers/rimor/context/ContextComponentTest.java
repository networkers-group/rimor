package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.ParamImpl;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.provide.builtin.Param;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContextComponentTest {

    static Token<String> stringToken = new Token<>(String.class);
    static Token<String> annotatedStringToken = new Token<>(String.class).withAnnotation(new ParamImpl(0));
    static Token<List<String>> stringListToken = new Token<>(new TypeToken<List<String>>() {});

    @Test
    void testSimpleComponent() {
        ContextComponent<String> component = new ContextComponent<>(String.class, "test");

        assertTrue(component.canProvide(stringToken));
        assertFalse(component.canProvide(annotatedStringToken));
    }

    @Test
    void testComplexComponent() {
        ContextComponent<List<String>> component = new ContextComponent<>(new TypeToken<List<String>>() {}, Collections.emptyList());

        assertTrue(component.canProvide(stringListToken));
    }

    @Test
    void testComponentWithAnnotationClass() {
        ContextComponent<String> component = new ContextComponent<>(Param.class, String.class, "test");

        assertTrue(component.canProvide(annotatedStringToken));
        assertFalse(component.canProvide(stringToken));
    }

    @Test
    void testComponentWithEqualAnnotation() {
        ContextComponent<String> component = new ContextComponent<>(new ParamImpl(0), String.class, "test");

        assertTrue(component.canProvide(annotatedStringToken));
        assertFalse(component.canProvide(stringToken));
    }

    @Test
    void testComponentWithNotEqualAnnotation() {
        ContextComponent<String> component = new ContextComponent<>(new ParamImpl(1), String.class, "test");

        assertFalse(component.canProvide(stringToken));
        assertFalse(component.canProvide(annotatedStringToken));
    }
}
