package st.networkers.rimor.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReflectionUtilsTest {

    public String bar = "bar";

    public static class StaticInnerClass {}

    @Test
    void givenStaticInnerClass_whenInstantiating_isInstantiated() {
        assertThat(ReflectionUtils.instantiateInnerClass(this, StaticInnerClass.class))
                .isInstanceOf(StaticInnerClass.class);
    }

    public class NonStaticInnerClass {
        public String getBar() {
            return bar;
        }
    }

    @Test
    void givenNonStaticInnerClass_whenInstantiating_isInstantiated() {
        assertThat(ReflectionUtils.instantiateInnerClass(this, NonStaticInnerClass.class))
                .isInstanceOf(NonStaticInnerClass.class)
                .extracting("bar")
                .containsExactly(bar);
    }

    static class InnerClassWithNoPublicConstructor {}

    @Test
    void givenInnerClassWithNoPublicConstructor_whenInstantiating_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> ReflectionUtils.instantiateInnerClass(this, InnerClassWithNoPublicConstructor.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("because it does not declare a public no-args constructor");
    }

    static class InnerClassWithOneArgConstructor {
        public InnerClassWithOneArgConstructor(int i) {
        }
    }

    @Test
    void givenInnerClassWithOneArgConstructor_whenInstantiating_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> ReflectionUtils.instantiateInnerClass(this, InnerClassWithOneArgConstructor.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("because it does not declare a public no-args constructor");
    }
}