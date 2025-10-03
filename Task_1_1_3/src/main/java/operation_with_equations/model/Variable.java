package operation_with_equations.model;

import java.util.Map;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Expression derivative(String var) {
        return name.equals(var) ? new Number(1) : new Number(0);
    }

    @Override
    public int eval(Map<String, Integer> values) {
        if (!values.containsKey(name)) {
            throw new IllegalArgumentException("Variable " + name + " not assigned a value.");
        }
        return values.get(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Variable)) return false;
        return name.equals(((Variable) obj).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}