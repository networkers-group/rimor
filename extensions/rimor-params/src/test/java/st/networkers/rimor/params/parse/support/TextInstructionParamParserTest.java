package st.networkers.rimor.params.parse.support;

import org.junit.jupiter.api.Test;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.params.InstructionParamImpl;
import st.networkers.rimor.params.parse.AbstractInstructionParamParser;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TextInstructionParamParserTest {

    @Test
    void givenTextParameterOfIndex0_whenGettingText_containsAllParams() {
        Token<String> token = Token.of(String.class)
                .annotatedWith(new InstructionParamImpl(null, null, 0))
                .annotatedWith(Text.class);

        ExecutionContext context = ExecutionContext.builder()
                .bind(AbstractInstructionParamParser.PARAMS_TOKEN, Arrays.asList("foo", "bar", "baz", "qux", "quux"))
                .build();

        assertThat(new TextInstructionParamParser().get(token, context)).isEqualTo("foo bar baz qux quux");
    }

    @Test
    void givenTextParameterOfIndex2_whenGettingText_containsAllButFirstTwoParams() {
        Token<String> token = Token.of(String.class)
                .annotatedWith(new InstructionParamImpl(null, null, 2))
                .annotatedWith(Text.class);

        ExecutionContext context = ExecutionContext.builder()
                .bind(AbstractInstructionParamParser.PARAMS_TOKEN, Arrays.asList("foo", "bar", "baz", "qux", "quux"))
                .build();

        assertThat(new TextInstructionParamParser().get(token, context)).isEqualTo("baz qux quux");
    }

    @Test
    void givenTextParameterOfIndexOutOfBounds_whenGettingText_returnsNull() {
        Token<String> token = Token.of(String.class)
                .annotatedWith(new InstructionParamImpl(null, null, 2))
                .annotatedWith(Text.class);

        ExecutionContext context = ExecutionContext.builder()
                .bind(AbstractInstructionParamParser.PARAMS_TOKEN, Arrays.asList("foo", "bar"))
                .build();

        assertThat(new TextInstructionParamParser().get(token, context)).isNull();
    }

    @Test
    void givenTextParameter_whenGettingText_stringRepresentationOfEachParamIsUsed() {
        Object mockedObject = mock(Object.class);
        when(mockedObject.toString()).thenReturn("baz");

        Token<String> token = Token.of(String.class)
                .annotatedWith(new InstructionParamImpl(null, null, 0))
                .annotatedWith(Text.class);

        ExecutionContext context = ExecutionContext.builder()
                .bind(AbstractInstructionParamParser.PARAMS_TOKEN, Arrays.asList("foo", "bar", mockedObject))
                .build();

        assertThat(new TextInstructionParamParser().get(token, context)).isEqualTo("foo bar baz");
    }
}
