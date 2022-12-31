package st.networkers.rimor.params.parse.builtin;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.ParamImpl;
import st.networkers.rimor.params.Params;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultParamParserTest {

    static DefaultParamParser defaultParamParser;
    static ExecutionContext context;

    @BeforeAll
    static void setUp() {
        defaultParamParser = new DefaultParamParser();

        ContextComponent<List<Object>> params = new ContextComponent<>(new TypeToken<List<Object>>() {}, Arrays.asList("foo", -1))
                .annotatedWith(Params.class);
        context = ExecutionContext.build(params);
    }

    @Test
    void parse() {
        Object object = new Object();
        assertEquals(object, defaultParamParser.parse(object, null, null));
        assertEquals("test", defaultParamParser.parse("test", null, null));
    }

    @Test
    void givenStringTokenAnnotatedWithParamWithPosition0_whenCheckingCanProvide_thenTrue() {
        Token<String> token = new Token<>(String.class).annotatedWith(new ParamImpl(0));
        assertTrue(defaultParamParser.canProvide(token, context));
    }

    @Test
    void givenIntegerTokenAnnotatedWithParamWithPosition1_whenCheckingCanProvide_thenTrue() {
        Token<Integer> token = new Token<>(Integer.class).annotatedWith(new ParamImpl(1));
        assertTrue(defaultParamParser.canProvide(token, context));
    }

    @Test
    void givenIntegerTokenAnnotatedWithParamWithPosition0_whenCheckingCanProvide_thenFalse() {
        Token<Integer> token = new Token<>(Integer.class).annotatedWith(new ParamImpl(0));
        assertFalse(defaultParamParser.canProvide(token, context));
    }
}
