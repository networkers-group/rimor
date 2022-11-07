package st.networkers.rimor.params.parse.builtin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringParamParserTest {

    static StringParamParser stringParamParser = new StringParamParser();

    @Test
    void parse() {
        assertEquals("test", stringParamParser.parse("test", null, null, null));
    }
}
