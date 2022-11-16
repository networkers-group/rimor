package st.networkers.rimor.util;

import st.networkers.rimor.command.CommandDefinition;
import st.networkers.rimor.command.CommandMapping;
import st.networkers.rimor.instruction.InstructionMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public final class InspectionUtils {

    private InspectionUtils() {
    }

    public static List<String> getAliases(Class<? extends CommandDefinition> definitionClass) {
        return Optional.ofNullable(definitionClass.getAnnotation(CommandMapping.class))
                .map(commandMapping -> Arrays.asList(commandMapping.value()))
                .orElseGet(() -> Collections.singletonList(definitionClass.getSimpleName()));
    }

    public static List<String> getAliases(Method method) {
        InstructionMapping instructionMapping = method.getAnnotation(InstructionMapping.class);
        if (instructionMapping == null)
            return Collections.singletonList(method.getName());

        List<String> aliases = new ArrayList<>(Arrays.asList(instructionMapping.value()));

        if (aliases.isEmpty() || !instructionMapping.ignoreMethodName())
            aliases.add(method.getName());

        return aliases;
    }

    public static Map<Class<? extends Annotation>, Annotation> getMappedAnnotations(Collection<Annotation> annotations) {
        Map<Class<? extends Annotation>, Annotation> annotationsMap = new HashMap<>();

        for (Annotation annotation : annotations)
            annotationsMap.put(annotation.annotationType(), annotation);

        return annotationsMap;
    }
}
