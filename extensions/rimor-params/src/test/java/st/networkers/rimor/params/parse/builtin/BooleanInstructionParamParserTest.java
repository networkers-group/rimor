package st.networkers.rimor.params.parse.builtin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import st.networkers.rimor.inject.Token;
import st.networkers.rimor.params.InstructionParam;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanInstructionParamParserTest {

    Token<Boolean> booleanToken;

    @BeforeEach
    void setUp() {
        booleanToken = Token.of(boolean.class).annotatedWith(InstructionParam.class);
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
        booleanToken = booleanToken.annotatedWith(new TrueValuesImpl("yes"));

        assertThat(parser.parse("yes", booleanToken, null)).isTrue();
        assertThat(parser.parse("yes!", booleanToken, null)).isFalse();
    }
}
