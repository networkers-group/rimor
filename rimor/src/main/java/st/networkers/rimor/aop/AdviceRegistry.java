package st.networkers.rimor.aop;

import st.networkers.rimor.inject.ExecutionContext;
import st.networkers.rimor.instruction.Instruction;
import st.networkers.rimor.util.HashMultiMap;
import st.networkers.rimor.util.MultiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdviceRegistry {

    private final MultiMap<AdviceType, Advice> advice;
    private final Cache<Throwable, ExceptionHandler<?>> exceptionHandlers;

    private final Map<Instruction, MultiMap<AdviceType, Advice>> mappedAdvice = new HashMap<>();

    public AdviceRegistry() {
        this(new HashMultiMap<>(), new HashMultiMap<>());
    }

    public AdviceRegistry(MultiMap<AdviceType, Advice> advice, MultiMap<Throwable, ExceptionHandler<?>> exceptionHandlers) {
        this.advice = advice;
        this.exceptionHandlers = exceptionHandlers;
    }

    public void registerBeforeAdvice(Advice advice) {
        registerAdvice(AdviceType.BEFORE, advice);
    }

    public void registerAfterReturnAdvice(Advice advice) {
        registerAdvice(AdviceType.AFTER_RETURN, advice);
    }

    public void registerAfterAdvice(Advice advice) {
        registerAdvice(AdviceType.AFTER, advice);
    }

    private void registerAdvice(AdviceType type, Advice advice) {
        this.advice.add(type, advice);
        // remove mappings for the given type
        this.mappedAdvice.values().forEach(a -> a.computeIfPresent(type, (k, v) -> null));
    }

    public void runBeforeAdvice(Instruction instruction, ExecutionContext context) {
        this.runAdvice(AdviceType.BEFORE, instruction, context);
    }

    public void runAfterReturnAdvice(Instruction instruction, ExecutionContext context) {
        this.runAdvice(AdviceType.AFTER_RETURN, instruction, context);
    }

    public void runAfterAdvice(Instruction instruction, ExecutionContext context) {
        this.runAdvice(AdviceType.AFTER, instruction, context);
    }

    private void runAdvice(AdviceType type, Instruction instruction, ExecutionContext context) {
        mappedAdvice.computeIfAbsent(instruction, i -> new HashMultiMap<>())
                .computeIfAbsent(type, t -> {
                    List<Advice> values = new ArrayList<>();
                    for (Advice advice : advice.get(type)) {
                        if (instruction.matchesAnnotations(advice))
                            values.add(advice);
                    }
                    return values;
                })
                .forEach(advice -> advice.run(instruction, context));
    }

    public enum AdviceType {
        BEFORE, AFTER_RETURN, AFTER
    }
}
