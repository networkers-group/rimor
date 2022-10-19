package st.networkers.rimor.context;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.ParamImpl;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.builtin.Param;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContextComponentTest {

    static Token<String> stringToken = new Token<>(String.class);
    static Token<String> annotatedStringToken = new Token<>(String.class).annotatedWith(new ParamImpl(0));

    @Test
    void givenStringComponent_whenCheckingIfCanProvideStringToken_thenTrue() {
        ContextComponent<String> component = new ContextComponent<>(String.class, "test");

        assertTrue(component.canProvide(stringToken));
    }

    @Test
    void givenStringComponent_whenCheckingIfCanProvideAnnotatedStringToken_thenFalse() {
        ContextComponent<String> component = new ContextComponent<>(String.class, "test");

        assertFalse(component.canProvide(annotatedStringToken));
    }

    @Test
    void givenAnnotatedStringComponent_whenCheckingIfCanProvideAnnotatedStringToken_thenTrue() {
        ContextComponent<String> component = new ContextComponent<>(String.class, "test")
                .annotatedWith(Param.class);

        assertTrue(component.canProvide(annotatedStringToken));
    }

    @Test
    void givenAnnotatedStringComponent_whenCheckingIfCanProvideStringToken_thenFalse() {
        ContextComponent<String> component = new ContextComponent<>(String.class, "test")
                .annotatedWith(Param.class);

        assertFalse(component.canProvide(stringToken));
    }

    @Test
    void givenAnnotatedStringComponent_whenCheckingIfCanProvideEquallyAnnotatedStringToken_thenTrue() {
        ContextComponent<String> component = new ContextComponent<>(String.class, "test")
                .annotatedWith(new ParamImpl(0));

        assertTrue(component.canProvide(annotatedStringToken));
    }

    @Test
    void givenAnnotatedStringComponent_whenCheckingIfCanProvideNotEquallyAnnotatedStringToken_thenTrue() {
        ContextComponent<String> component = new ContextComponent<>(String.class, "test")
                .annotatedWith(new ParamImpl(1));

        assertFalse(component.canProvide(annotatedStringToken));
    }

    @Test
    void givenStringArrayListComponent_whenCheckingIfCanProvideStringArrayListToken_thenTrue() {
        ContextComponent<ArrayList<String>> component = new ContextComponent<>(new TypeToken<ArrayList<String>>() {}, new ArrayList<>());
        Token<ArrayList<String>> stringListToken = new Token<>(new TypeToken<ArrayList<String>>() {});

        assertTrue(component.canProvide(stringListToken));
    }

    @Test
    void givenStringArrayListComponent_whenCheckingIfCanProvideStringArrayListSupertypeToken_thenTrue() {
        ContextComponent<ArrayList<String>> component = new ContextComponent<>(new TypeToken<ArrayList<String>>() {}, new ArrayList<>());
        Token<List<String>> token = new Token<>(new TypeToken<List<String>>() {});

        assertTrue(component.canProvide(token));
    }

    @Test
    void givenStringListComponent_whenCheckingIfCanProvideIntegerListToken_thenFalse() {
        ContextComponent<List<String>> component = new ContextComponent<>(new TypeToken<List<String>>() {}, Collections.emptyList());
        Token<List<Integer>> token = new Token<>(new TypeToken<List<Integer>>() {});

        assertFalse(component.canProvide(token));
    }
}
