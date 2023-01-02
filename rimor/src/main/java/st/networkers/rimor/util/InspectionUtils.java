package st.networkers.rimor.util;

import st.networkers.rimor.command.CommandMapping;
import st.networkers.rimor.command.RimorCommand;
import st.networkers.rimor.instruction.InstructionMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class InspectionUtils {

    private InspectionUtils() {
    }

    public static Optional<String> getName(Class<? extends RimorCommand> commandClass) {
        return Optional.ofNullable(commandClass.getAnnotation(CommandMapping.class))
                .map(CommandMapping::name)
                .map(name -> name.isEmpty() ? null : name);
    }

    public static List<String> getAliases(Class<? extends RimorCommand> commandClass) {
        CommandMapping commandMapping = commandClass.getAnnotation(CommandMapping.class);

        if (commandMapping == null)
            return Collections.singletonList(commandClass.getSimpleName());

        if (commandMapping.value().length == 0) {
            String name = commandMapping.name();

            if (name.isEmpty())
                throw new IllegalArgumentException("the specified aliases for " + commandClass.getSimpleName() + " are empty");
            return Collections.singletonList(name);
        }

        return Arrays.asList(commandMapping.value());
    }

    public static List<String> getAliases(Method method) {
        InstructionMapping instructionMapping = method.getAnnotation(InstructionMapping.class);
        List<String> aliases = new ArrayList<>(Arrays.asList(instructionMapping.value()));

        if (aliases.isEmpty() || !instructionMapping.ignoreMethodName())
            aliases.add(method.getName());

        return aliases;
    }

    public static Map<Class<? extends Annotation>, Annotation> getMappedAnnotations(Collection<Annotation> annotations) {
        return annotations.stream().collect(Collectors.toMap(Annotation::annotationType, Function.identity()));
    }
}
