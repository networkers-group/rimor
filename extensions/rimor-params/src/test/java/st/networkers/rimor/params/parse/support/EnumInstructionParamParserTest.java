package st.networkers.rimor.params.parse.support;

import org.junit.jupiter.api.Test;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.params.InstructionParam;

import static org.assertj.core.api.Assertions.assertThat;

class EnumInstructionParamParserTest {

    enum FooEnum {
        FOO, BAR
    }

    static Token<FooEnum> fooEnumToken = Token.of(FooEnum.class).annotatedWith(InstructionParam.class);

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

    @SuppressWarnings("unchecked")
    static Enum<?> parse(Object parameter, Token<?> token) {
        return new EnumInstructionParamParser().parse(parameter, (Token<Enum<?>>) token, null);
    }
}
