package st.networkers.rimor.context;

import org.apache.commons.lang3.reflect.TypeUtils;
import st.networkers.rimor.qualify.Token;
import st.networkers.rimor.util.MatchingMap;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * Contains information relative to the execution of a command (for example, its parameters, the executor...).
 */
public class ExecutionContext {

    public static Builder builder() {
        return new Builder();
    }

    // not a simple map because there may be components bound to tokens with required qualifier types,
    // and we have to provide them to parameters with instances of them
    final MatchingMap<Token, Object> components;

    public ExecutionContext(MatchingMap<Token, Object> components) {
        this.components = components;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(Token token) {
        return (Optional<T>) Optional.ofNullable(components.get(token));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecutionContext)) return false;
        ExecutionContext context = (ExecutionContext) o;
        return Objects.equals(components, context.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components);
    }

    public static class Builder {
        private final MatchingMap<Token, Object> components;

        private Builder() {
            this.components = new MatchingMap<>();
        }

        public <T> Builder bind(Class<? super T> type, T object) {
            return this.bind(Token.of(type), object);
        }

        public Builder bind(Type type, Object object) {
            return this.bind(Token.of(type), object);
        }

        public Builder bind(Token token, Object object) {
            if (!TypeUtils.isAssignable(object.getClass(), token.getType())) {
                throw new IllegalArgumentException("trying to bind " + object + " to a token with not assignable type " + token.getType());
            }

            this.components.put(token, object);
            return this;
        }

        public Builder withBindingsOf(ExecutionContext context) {
            this.components.putAll(context.components);
            return this;
        }

        public ExecutionContext build() {
            return new ExecutionContext(this.components);
        }
    }
}
