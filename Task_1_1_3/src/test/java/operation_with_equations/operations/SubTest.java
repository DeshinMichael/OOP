package operation_with_equations.operations;

import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SubTest {

    @Test
    void testEval() {
        Expression left = new Number(10);
        Expression right = new Number(4);
        Sub sub = new Sub(left, right);

        Map<String, Integer> values = new HashMap<>();
        assertEquals(6, sub.eval(values));
    }

    @Test
    void testEvalWithVariables() {
        Expression left = new Variable("x");
        Expression right = new Variable("y");
        Sub sub = new Sub(left, right);

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 15);
        values.put("y", 7);

        assertEquals(8, sub.eval(values));
    }

    @Test
    void testEvalWithNegativeResult() {
        Expression left = new Number(5);
        Expression right = new Number(10);
        Sub sub = new Sub(left, right);

        assertEquals(-5, sub.eval(new HashMap<>()));
    }

    @Test
    void testDerivative() {
        Expression x = new Variable("x");
        Expression num = new Number(5);
        Sub sub = new Sub(x, num);

        // производная (x-5) по x = 1-0 = 1
        Expression derivative = sub.derivative("x");
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(1, derivative.eval(values));
    }

    @Test
    void testDerivativeComplex() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Sub sub = new Sub(x, y);

        // производная (x-y) по x = 1-0 = 1
        Expression derivative = sub.derivative("x");
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 7);
        values.put("y", 3);
        assertEquals(1, derivative.eval(values));

        // производная (x-y) по y = 0-1 = -1
        derivative = sub.derivative("y");
        assertEquals(-1, derivative.eval(values));
    }

    @Test
    void testToString() {
        Expression left = new Number(8);
        Expression right = new Number(3);
        Sub sub = new Sub(left, right);

        assertEquals("(8 - 3)", sub.toString());
    }

    @Test
    void testEquals() {
        Expression a1 = new Number(10);
        Expression b1 = new Number(5);
        Sub sub1 = new Sub(a1, b1);

        Expression a2 = new Number(10);
        Expression b2 = new Number(5);
        Sub sub2 = new Sub(a2, b2);

        Expression a3 = new Number(5);
        Expression b3 = new Number(10);
        Sub sub3 = new Sub(a3, b3);

        Expression a4 = new Number(8);
        Expression b4 = new Number(3);
        Sub sub4 = new Sub(a4, b4);

        assertTrue(sub1.equals(sub1)); // Рефлексивность
        assertTrue(sub1.equals(sub2)); // Одинаковые выражения
        assertFalse(sub1.equals(sub3)); // Вычитание не коммутативно
        assertFalse(sub1.equals(sub4)); // Разные выражения
        assertFalse(sub1.equals(null)); // Сравнение с null
        assertFalse(sub1.equals(new Add(a1, b1))); // Разные типы операций
    }

    @Test
    void testHashCode() {
        Expression a1 = new Number(10);
        Expression b1 = new Number(5);
        Sub sub1 = new Sub(a1, b1);

        Expression a2 = new Number(10);
        Expression b2 = new Number(5);
        Sub sub2 = new Sub(a2, b2);

        // Одинаковые выражения должны иметь одинаковые хэш-коды
        assertEquals(sub1.hashCode(), sub2.hashCode());
    }

    @Test
    void testSimplify() {
        Expression a = new Number(10);
        Expression b = new Number(3);
        Sub sub = new Sub(a, b);
        Expression simplified = sub.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(7, simplified.eval(new HashMap<>()));

        Expression x = new Variable("x");
        Expression num = new Number(5);
        Sub subWithVar = new Sub(x, num);
        Expression simplifiedWithVar = subWithVar.simplify();
        assertTrue(simplifiedWithVar instanceof Sub);
        assertEquals("(x - 5)", simplifiedWithVar.toString());

        Sub subSame = new Sub(x, x);
        Expression simplifiedSame = subSame.simplify();
        assertTrue(simplifiedSame instanceof Number);
        assertEquals(0, simplifiedSame.eval(new HashMap<>()));

        Expression innerSub = new Sub(new Number(8), new Number(3));
        Sub outerSub = new Sub(innerSub, new Number(2));
        Expression simplifiedComplex = outerSub.simplify();
        assertTrue(simplifiedComplex instanceof Number);
        assertEquals(3, simplifiedComplex.eval(new HashMap<>())); // (8-3)-2 = 5-2 = 3
    }
}
