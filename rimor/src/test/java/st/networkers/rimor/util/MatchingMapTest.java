package st.networkers.rimor.util;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class MatchingMapTest {

    // Foo matches another Foo if their strings are equal ignoring case and the first's int is greater or equal
    // than the second's
    static class Foo implements MatchingKey {
        String string;
        int number;

        public Foo(String string, int number) {
            this.string = string;
            this.number = number;
        }

        @Override
        public boolean matches(MatchingKey other) {
            if (!(other instanceof Foo))
                return false;

            return this.string.equalsIgnoreCase(((Foo) other).string) && this.number >= ((Foo) other).number;
        }

        @Override
        public int matchingHashCode() {
            return Objects.hash(string.toLowerCase());
        }
    }

    @Test
    void givenFooObjectWithCompletelyDifferentStringThanAnythingPresentInMap_returnsNull() {
        MatchingMap<Foo, String> matchingMap = new MatchingMap<>();

        matchingMap.put(new Foo("key", -1), "value");

        assertThat(matchingMap.get(new Foo("absentKey", -1))).isNull();
    }

    @Test
    void givenFooObjectPresentInMapWithStringInLowercase_returnsTheValueAssignedToThatKey() {
        MatchingMap<Foo, String> matchingMap = new MatchingMap<>();

        matchingMap.put(new Foo("key", -1), "value");

        assertThat(matchingMap.get(new Foo("KEY", -1))).isEqualTo("value");
    }

    @Test
    void givenFooObjectPresentInMapWithStringInLowercaseAndSmallerNumber_returnsTheValueAssignedToThatKey() {
        MatchingMap<Foo, String> matchingMap = new MatchingMap<>();

        matchingMap.put(new Foo("key", -1), "value");

        assertThat(matchingMap.get(new Foo("KEY", 1))).isEqualTo("value");
    }
}