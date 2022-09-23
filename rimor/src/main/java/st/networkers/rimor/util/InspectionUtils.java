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

    @NotNull
    public static List<String> getAliases(@NotNull AnnotatedElement object) {
        Aliases aliases = object.getAnnotation(Aliases.class);
        return aliases == null ? Collections.emptyList() : Arrays.asList(aliases.value());
    }
}
