package st.networkers.rimor.util;

import org.jetbrains.annotations.NotNull;
import st.networkers.rimor.command.Aliases;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class InspectionUtils {

    private InspectionUtils() {
    }

    /**
     * Returns an immutable list of the aliases annotated on the given element, or an empty list if there is no
     * {@link Aliases} annotation present.
     *
     * @param element the element to get its aliases
     * @return an immutable list of the aliases annotated on the given element, or an empty list if there is no
     * {@link Aliases} annotation present
     */
    @NotNull
    public static List<String> getAliases(@NotNull AnnotatedElement element) {
        Aliases aliases = element.getAnnotation(Aliases.class);
        return aliases == null ? Collections.emptyList() : Arrays.asList(aliases.value());
    }
}
