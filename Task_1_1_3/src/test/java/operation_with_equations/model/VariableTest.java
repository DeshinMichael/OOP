package operation_with_equations.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class VariableTest {

    @Test
    void testDerivativeSameVariable() {
        Variable var = new Variable("x");
        Expression derivative = var.derivative("x");

        assertTrue(derivative instanceof Number);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(1, derivative.eval(emptyMap));
    }

    @Test
    void testDerivativeDifferentVariable() {
        Variable var = new Variable("x");
        Expression derivative = var.derivative("y");

        assertTrue(derivative instanceof Number);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(0, derivative.eval(emptyMap));
    }

    @Test
    void testEval() {
        Variable var = new Variable("x");
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 42);

        assertEquals(42, var.eval(values));
    }

    @Test
    void testEvalMissingVariable() {
        Variable var = new Variable("x");
        Map<String, Integer> values = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            var.eval(values);
        });
    }

    @Test
    void testEvalNullValues() {
        Variable var = new Variable("x");

        assertThrows(NullPointerException.class, () -> {
            var.eval((Map<String, Integer>) null);
        });
    }

    @Test
    void testEquals() {
        Variable var1 = new Variable("x");
        Variable var2 = new Variable("x");
        Variable var3 = new Variable("y");

        assertTrue(var1.equals(var1)); // Рефлексивность
        assertTrue(var1.equals(var2)); // Одинаковые имена
        assertFalse(var1.equals(var3)); // Разные имена
        assertFalse(var1.equals(new Number(42))); // Разные классы
        assertFalse(var1.equals(null)); // Сравнение с null
    }

    @Test
    void testHashCode() {
        Variable var1 = new Variable("x");
        Variable var2 = new Variable("x");
        Variable var3 = new Variable("y");

        // Одинаковые переменные должны иметь одинаковые хэш-коды
        assertEquals(var1.hashCode(), var2.hashCode());

        // Разные переменные должны иметь разные хэш-коды
        assertNotEquals(var1.hashCode(), var3.hashCode());

        // Хэш-код должен соответствовать строке имени
        assertEquals("x".hashCode(), var1.hashCode());
    }

    @Test
    void testToString() {
        Variable var = new Variable("x");
        assertEquals("x", var.toString());

        Variable longVar = new Variable("variable_name");
        assertEquals("variable_name", longVar.toString());
    }
}
