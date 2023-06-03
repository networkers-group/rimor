package st.networkers.rimor.util;

import com.google.common.reflect.TypeToken;
import st.networkers.rimor.annotated.RequireAnnotationTypes;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Map<Class<? extends Annotation>, Annotation> getMappedAnnotations(AnnotatedElement element) {
        List<Annotation> annotations = new ArrayList<>(Arrays.asList(element.getAnnotations()));
        annotations.removeIf(annotation -> annotation instanceof RequireAnnotationTypes);
        return annotations.stream().collect(Collectors.toMap(Annotation::annotationType, Function.identity()));
    }

    public static TypeToken<?> unwrapOptional(TypeToken<Optional<?>> optionalTypeToken) {
        return optionalTypeToken.resolveType(Optional.class.getTypeParameters()[0]);
    }

    public static Object instantiateInnerClass(Object enclosingInstance, Class<?> innerClass) {
        try {
            return Modifier.isStatic(innerClass.getModifiers())
                    ? innerClass.getConstructor().newInstance()
                    : innerClass.getConstructor(enclosingInstance.getClass()).newInstance(enclosingInstance);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Cannot instantiate " + innerClass.getName() + " class because it " +
                                               "does not declare a public no-args constructor", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke(Method method, Object instance, Object[] parameters) {
        try {
            return method.invoke(instance, parameters);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }
}
