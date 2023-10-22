package st.networkers.rimor.params.parse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import st.networkers.rimor.context.ExecutionContext;
import st.networkers.rimor.params.InstructionParam;
import st.networkers.rimor.params.InstructionParamImpl;
import st.networkers.rimor.params.InstructionParams;
import st.networkers.rimor.params.parse.support.DefaultInstructionParamParser;
import st.networkers.rimor.qualify.ParameterToken;
import st.networkers.rimor.qualify.ParameterizedToken;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;
import st.networkers.rimor.qualify.reflect.QualifiedParameter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractInstructionParamParserTest {

    static List<Object> params;
    static ExecutionContext context;

    @BeforeAll
    static void beforeAll() {
        params = Arrays.asList("bar", true, -1, null, "baz");
        context = ExecutionContext.builder()
                .bind(new ParameterizedToken<List<Object>>() {}.qualifiedWith(InstructionParams.class), params)
                .build();
    }

    @Test
    void whenGettingInstructionParamByProvidedIndex_returnsInstructionParamByProvidedIndex() {
        Token token = Token.of(Object.class)
                .qualifiedWith(new InstructionParamImpl("", "", 4));
        assertThat(new DefaultInstructionParamParser().get(token, context)).isEqualTo(params.get(4));
    }

    @Test
    void whenGettingInstructionParamWithIndexOutOfBounds_returnsNull() {
        Token token = Token.of(Object.class)
                .qualifiedWith(new InstructionParamImpl("", "", 10));

        assertThat(new DefaultInstructionParamParser().get(token, context)).isNull();
    }

    void foo(Object thisShouldNotBeCounted,
             @InstructionParam String param0,
             @InstructionParam boolean param1,
             @InstructionParam int param2,
             @InstructionParam(index = 4) String param4,
             @InstructionParam Object param3) {
    }

    @ParameterizedTest
    @MethodSource
    void whenGettingInstructionParamByMethodParameterOrder_returnsInstructionParamByParameterOrder(QualifiedMethod qualifiedMethod,
                                                                                                   QualifiedParameter qualifiedParameter,
                                                                                                   Object expectedParameter) {
        Token token = ParameterToken.build(qualifiedMethod, qualifiedParameter);
        assertThat(new DefaultInstructionParamParser().get(token, context)).isEqualTo(expectedParameter);
    }

    public static Stream<Arguments> whenGettingInstructionParamByMethodParameterOrder_returnsInstructionParamByParameterOrder() throws NoSuchMethodException {
        QualifiedMethod method = QualifiedMethod.build(AbstractInstructionParamParserTest.class.getDeclaredMethod("foo", Object.class, String.class, boolean.class, int.class, String.class, Object.class));
        List<QualifiedParameter> methodParameters = method.getQualifiedParameters();
        return Stream.of(
                Arguments.of(method, methodParameters.get(1), params.get(0)),
                Arguments.of(method, methodParameters.get(2), params.get(1)),
                Arguments.of(method, methodParameters.get(3), params.get(2)),
                Arguments.of(method, methodParameters.get(5), params.get(3))
        );
    }
}
