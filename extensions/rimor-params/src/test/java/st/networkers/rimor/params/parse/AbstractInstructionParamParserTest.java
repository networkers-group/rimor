package st.networkers.rimor.params.parse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.context.Token;
import st.networkers.rimor.context.ParameterToken;
import st.networkers.rimor.params.InstructionParam;
import st.networkers.rimor.params.InstructionParamImpl;
import st.networkers.rimor.params.InstructionParams;
import st.networkers.rimor.params.parse.support.DefaultInstructionParamParser;
import st.networkers.rimor.reflect.CachedMethod;
import st.networkers.rimor.reflect.CachedParameter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
class AbstractInstructionParamParserTest {

    void foo(Object thisShouldNotBeCounted,
             @InstructionParam String param0,
             @InstructionParam boolean param1,
             @InstructionParam int param2,
             @InstructionParam(index = 4) String param4,
             @InstructionParam Object param3) {
    }

    static CachedMethod fooMethod;
    static ExecutionContext context;

    static List<Object> params = Arrays.asList("bar", true, -1, null, "baz");

    @BeforeAll
    static void beforeAll() throws NoSuchMethodException {
        fooMethod = CachedMethod.build(AbstractInstructionParamParserTest.class.getDeclaredMethod("foo", Object.class, String.class, boolean.class, int.class, String.class, Object.class));
        context = ExecutionContext.builder()
                .bind(new Token<List<Object>>() {}.annotatedWith(InstructionParams.class), params)
                .build();
    }

    @Test
    void whenGettingInstructionParamByProvidedIndex_returnsInstructionParamByProvidedIndex() {
        Token<Object> token = (Token<Object>) ParameterToken.build(fooMethod, fooMethod.getParameters().get(4));
        assertThat(new DefaultInstructionParamParser().get(token, context)).isEqualTo(params.get(4));
    }

    @Test
    void whenGettingInstructionParamWithIndexOutOfBounds_returnsNull() {
        Token<Object> token = Token.of(Object.class)
                .annotatedWith(new InstructionParamImpl("", "", 10));

        assertThat(new DefaultInstructionParamParser().get(token, context)).isNull();
    }

    @ParameterizedTest
    @MethodSource
    void whenGettingInstructionParamByMethodParameterOrder_returnsInstructionParamByParameterOrder(CachedParameter parameter, Object expectedParameter) {
        Token<Object> token = (Token<Object>) ParameterToken.build(fooMethod, parameter);
        assertThat(new DefaultInstructionParamParser().get(token, context)).isEqualTo(expectedParameter);
    }

    public static Stream<Arguments> whenGettingInstructionParamByMethodParameterOrder_returnsInstructionParamByParameterOrder() {
        return Stream.of(
                Arguments.of(fooMethod.getParameters().get(1), params.get(0)),
                Arguments.of(fooMethod.getParameters().get(2), params.get(1)),
                Arguments.of(fooMethod.getParameters().get(3), params.get(2)),
                Arguments.of(fooMethod.getParameters().get(5), params.get(3))
        );
    }
}
