package st.networkers.rimor.params.parse.builtin;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.ParamImpl;
import st.networkers.rimor.params.Params;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PresentObjectParamParserTest {

    static PresentObjectParamParser objectParamParser = new PresentObjectParamParser();

    @Test
    void parse() {
        assertEquals("test", objectParamParser.parse("test", null, null, null));

        Object o = new Object();
        assertEquals(o, objectParamParser.parse(o, null, null, null));
    }

    @Test
    void canProvide() {
        List<Object> params = Arrays.asList("foo", objectParamParser);

        ExecutionContext context = ExecutionContext.build(
                new ContextComponent<>(new TypeToken<List<Object>>() {}, params).annotatedWith(Params.class)
        );

        assertTrue(objectParamParser.canProvide(
                new Token<>(String.class).annotatedWith(new ParamImpl(0)),
                null,
                context
        ));

        assertTrue(objectParamParser.canProvide(
                new Token<>(PresentObjectParamParser.class).annotatedWith(new ParamImpl(1)),
                null,
                context
        ));

        assertFalse(objectParamParser.canProvide(
                new Token<>(Integer.class).annotatedWith(new ParamImpl(0)),
                null,
                context
        ));
    }
}
