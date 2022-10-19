package st.networkers.rimor.internal.provide.builtin;

import org.junit.jupiter.api.Test;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.provide.builtin.Param;

import static org.junit.jupiter.api.Assertions.*;

class EnumParamParserTest {

    enum TestEnum {
        TEST_1, TEST_2
    }

    static EnumParamParser enumParamParser = new EnumParamParser();
    static Token<TestEnum> token = new Token<>(TestEnum.class).annotatedWith(Param.class);

    @Test
    void parse() {
        assertEquals(TestEnum.TEST_1, parse("TEST_1", token));
        assertEquals(TestEnum.TEST_2, parse("TEST_2", token));
        assertNull(parse("TEST_3", token));
    }

    @Test
    void canProvide() {
        assertTrue(enumParamParser.canProvide(token));
        assertFalse(enumParamParser.canProvide(new Token<>(TestEnum.class))); // does not have Param annotation
        assertFalse(enumParamParser.canProvide(new Token<>(String.class)));
    }

    @SuppressWarnings("unchecked")
    static <T extends Enum<T>> Enum<?> parse(String parameter, Token<T> token) {
        return enumParamParser.parse(parameter, (Token<Enum<?>>) token, null, null);
    }
}
