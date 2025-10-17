package operation_with_equations.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Expression {

    public abstract Expression derivative(String var);

    public int eval(String assignments) {
        Map<String, Integer> values = parseAssignments(assignments);
        return eval(values);
    }

    public abstract int eval(Map<String, Integer> values);

    public abstract Expression simplify();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    public void print() {
        System.out.println(this);
    }

    @Override
    public abstract String toString();

    private Map<String, Integer> parseAssignments(String assignments) {
        Map<String, Integer> values = new HashMap<>();
        if (assignments == null || assignments.trim().isEmpty()) {
            return values;
        }

        String[] pairs = assignments.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String varName = keyValue[0].trim();
                int value = Integer.parseInt(keyValue[1].trim());
                values.put(varName, value);
            }
        }
        return values;
    }
}