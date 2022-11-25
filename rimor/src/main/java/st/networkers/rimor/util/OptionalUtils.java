package st.networkers.rimor.util;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class OptionalUtils {

    private OptionalUtils() {
    }

    // Java 8 optionals don't have #or :(
    @SafeVarargs
    public static <T> Optional<T> firstPresent(Optional<T> first, Supplier<Optional<T>>... optionals) {
        return first.isPresent() ? first : firstPresent(optionals);
    }

    @SafeVarargs
    public static <T> Optional<T> firstPresent(Supplier<Optional<T>>... optionals) {
        for (Supplier<Optional<T>> supplier : optionals) {
            Optional<T> optional = supplier.get();
            if (optional.isPresent())
                return optional;
        }

        return Optional.empty();
    }
}
