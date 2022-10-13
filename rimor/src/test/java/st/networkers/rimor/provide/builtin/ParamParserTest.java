package st.networkers.rimor.provide.builtin;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.ParamImpl;
import st.networkers.rimor.context.ContextComponent;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.internal.inject.Injector;
import st.networkers.rimor.internal.inject.Token;
import st.networkers.rimor.internal.provide.builtin.EnumParamParser;
import st.networkers.rimor.internal.provide.builtin.StringParamParser;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParamParserTest {

    enum TestEnum {
        TEST_1, TEST_2
    }

    static Injector injector = new Injector()
            .registerProvider(new StringParamParser())
            .registerProvider(new EnumParamParser());

    static ExecutionContext context = ExecutionContext.build(
            new ContextComponent<>(new TypeToken<List<String>>() {}, Arrays.asList("test", "TEST_2"))
                    .annotatedWith(Params.class)
    );

    @Test
    void testStringProvider() {
        assertEquals("test", injector.get(
                new Token<>(String.class).annotatedWith(new ParamImpl(0)),
                context
        ));
    }

    @Test
    void testEnumProvider() {
        assertEquals(TestEnum.TEST_2, injector.get(
                new Token<>(TestEnum.class).annotatedWith(new ParamImpl(1)),
                context
        ));
    }
}
