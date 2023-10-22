package st.networkers.rimor.params.parse.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.params.InstructionParam;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanInstructionParamParserTest {

    Token booleanToken;

    @BeforeEach
    void setUp() {
        booleanToken = Token.of(boolean.class).qualifiedWith(InstructionParam.class);
    }

    @Test
    void givenBoolean_whenParsing_returnsGivenBoolean() {
        BooleanInstructionParamParser parser = new BooleanInstructionParamParser();

        assertThat(parser.parse(true, booleanToken, null)).isTrue();
        assertThat(parser.parse(false, booleanToken, null)).isFalse();
    }

    @Test
    void givenStringRepresentationOfBoolean_whenParsing_returnsBooleanWithGivenStringRepresentation() {
        BooleanInstructionParamParser parser = new BooleanInstructionParamParser();

        assertThat(parser.parse("true", booleanToken, null)).isTrue();
        assertThat(parser.parse("false", booleanToken, null)).isFalse();
    }

    @Test
    void givenRegisteredTrueAlias_whenParsing_returnsTrue() {
        BooleanInstructionParamParser parser = new BooleanInstructionParamParser("yes");

        assertThat(parser.parse("yes", booleanToken, null)).isTrue();
        assertThat(parser.parse("yes!", booleanToken, null)).isFalse();
    }

    @Test
    void givenValuePresentInTrueValuesAnnotationInToken_whenParsing_returnsTrue() {
        BooleanInstructionParamParser parser = new BooleanInstructionParamParser();
        booleanToken = booleanToken.qualifiedWith(new TrueValuesImpl("yes"));

        assertThat(parser.parse("yes", booleanToken, null)).isTrue();
        assertThat(parser.parse("yes!", booleanToken, null)).isFalse();
    }
}
