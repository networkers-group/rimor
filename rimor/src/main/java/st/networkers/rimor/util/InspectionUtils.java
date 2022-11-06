package st.networkers.rimor.util;

import st.networkers.rimor.command.Aliases;
import st.networkers.rimor.instruction.IgnoreMethodName;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

public final class InspectionUtils {

    private InspectionUtils() {
    }

    /**
     * Returns a list of the aliases annotated with {@link Aliases} on the given element.
     * <p>
     * If the given element is a {@link Method} and it is not annotated with {@link IgnoreMethodName}, the returned list
     * will also contain the name of the method.
     * <p>
     * In case there are no annotated aliases and the given element is a {@link Class} or a {@link Method},
     * the returned list will contain the name of the class or method.
     *
     * @param element the element to get its aliases
     * @return a list of the aliases annotated on the given element.
     */
    public static List<String> getAliases(AnnotatedElement element) {
        List<String> aliases = Optional.ofNullable(element.getAnnotation(Aliases.class))
                .map(a -> new ArrayList<>(Arrays.asList(a.value())))
                .orElseGet(ArrayList::new);

        if (element instanceof Class<?>) {
            Class<?> clazz = (Class<?>) element;
            if (aliases.isEmpty())
                return Collections.singletonList(clazz.getSimpleName());
        }

        if (element instanceof Method) {
            Method method = (Method) element;
            if (aliases.isEmpty() || !element.isAnnotationPresent(IgnoreMethodName.class))
                aliases.add(method.getName());
        }

        return aliases;
    }

    public static Map<Class<? extends Annotation>, Annotation> getMappedAnnotations(Collection<Annotation> annotations) {
        Map<Class<? extends Annotation>, Annotation> annotationsMap = new HashMap<>();

        for (Annotation annotation : annotations)
            annotationsMap.put(annotation.annotationType(), annotation);

        return annotationsMap;
    }
}
