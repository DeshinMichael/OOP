package operation_with_equations.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class NumberTest {

    @Test
    void testDerivative() {
        Number num = new Number(42);
        Expression derivative = num.derivative("x");

        assertTrue(derivative instanceof Number);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(0, derivative.eval(emptyMap));
    }

    @Test
    void testEval() {
        Number num = new Number(42);
        Map<String, Integer> values = new HashMap<>();

        assertEquals(42, num.eval(values));

        // Проверка с пустым Map вместо null
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(42, num.eval(emptyMap));
    }

    @Test
    void testEquals() {
        Number num1 = new Number(42);
        Number num2 = new Number(42);
        Number num3 = new Number(100);

        assertTrue(num1.equals(num1)); // Рефлексивность
        assertTrue(num1.equals(num2)); // Одинаковые значения
        assertFalse(num1.equals(num3)); // Разные значения
        assertFalse(num1.equals(new Variable("x"))); // Разные классы
        assertFalse(num1.equals(null)); // Сравнение с null
    }

    @Test
    void testHashCode() {
        Number num1 = new Number(42);
        Number num2 = new Number(42);
        Number num3 = new Number(100);

        // Одинаковые числа должны иметь одинаковые хэш-коды
        assertEquals(num1.hashCode(), num2.hashCode());

        // Разные числа должны иметь разные хэш-коды
        assertNotEquals(num1.hashCode(), num3.hashCode());

        // Хэш-код должен соответствовать Integer.hashCode
        assertEquals(Integer.hashCode(42), num1.hashCode());
    }

    @Test
    void testToString() {
        Number num = new Number(42);
        assertEquals("42", num.toString());

        Number negativeNum = new Number(-10);
        assertEquals("-10", negativeNum.toString());

        Number zeroNum = new Number(0);
        assertEquals("0", zeroNum.toString());
    }
}
