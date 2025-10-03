package operation_with_equations.model;

import java.util.Map;

public class Number extends Expression {
    private final int value;

    public Number(int value) {
        this.value = value;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    @Override
    public int eval(Map<String, Integer> values) {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Number)) return false;
        return value == ((Number) obj).value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}