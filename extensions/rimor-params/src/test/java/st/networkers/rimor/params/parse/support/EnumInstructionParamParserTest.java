package st.networkers.rimor.params.parse.support;

import org.junit.jupiter.api.Test;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.params.InstructionParam;

import static org.assertj.core.api.Assertions.assertThat;

class EnumInstructionParamParserTest {

    enum FooEnum {
        FOO, BAR
    }

    static Token fooEnumToken = Token.of(FooEnum.class).qualifiedWith(InstructionParam.class);

    @Test
    void givenEnumConstant_whenParsing_resultEqualsGivenConstant() {
        assertThat(parse(FooEnum.FOO, fooEnumToken)).isEqualTo(FooEnum.FOO);
    }

    @Test
    void givenEnumConstantName_whenParsing_resultEqualsConstantWithGivenName() {
        assertThat(parse("FOO", fooEnumToken)).isEqualTo(FooEnum.FOO);
    }

    @Test
    void givenEnumConstantLowercaseName_whenParsing_resultEqualsConstantWithGivenNameIgnoringCase() {
        assertThat(parse("bar", fooEnumToken)).isEqualTo(FooEnum.BAR);
    }

    @Test
    void givenStringThatDoesNotMatchAnyEnumConstant_whenParsing_resultIsNull() {
        assertThat(parse("QUX", fooEnumToken)).isNull();
    }

    static Enum<?> parse(Object parameter, Token token) {
        return new EnumInstructionParamParser().parse(parameter, token, null);
    }
}
