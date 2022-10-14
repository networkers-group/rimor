package st.networkers.rimor.internal.provide.builtin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BooleanParamParserTest {

    static BooleanParamParser booleanParamParser = new BooleanParamParser("yes");

    @Test
    void parse() {
        assertTrue(booleanParamParser.parse("true", null, null, null));
        assertTrue(booleanParamParser.parse("yes", null, null, null));
        assertFalse(booleanParamParser.parse("false", null, null, null));
        assertFalse(booleanParamParser.parse("no", null, null, null));
    }
}
