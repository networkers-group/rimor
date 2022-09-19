package st.networkers.rimor.util;

import org.jetbrains.annotations.NotNull;
import st.networkers.rimor.command.Aliases;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

public final class InspectionUtils {

    private InspectionUtils() {
    }

    @NotNull
    public static List<String> getAliases(@NotNull AnnotatedElement object) {
        return Arrays.asList(object.getAnnotation(Aliases.class).value());
    }
}
