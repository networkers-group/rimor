package st.networkers.rimor.util;

public interface MatchingKey {

    boolean matches(MatchingKey other);

    int matchingHashCode();

}
