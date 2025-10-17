package operation_with_equations.operations;

import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MulTest {

    @Test
    void testEval() {
        Expression left = new Number(5);
        Expression right = new Number(7);
        Mul mul = new Mul(left, right);

        Map<String, Integer> values = new HashMap<>();
        assertEquals(35, mul.eval(values));
    }

    @Test
    void testEvalWithVariables() {
        Expression left = new Variable("x");
        Expression right = new Variable("y");
        Mul mul = new Mul(left, right);

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 6);
        values.put("y", 8);

        assertEquals(48, mul.eval(values));
    }

    @Test
    void testDerivative() {
        Expression x = new Variable("x");
        Expression num = new Number(5);
        Mul mul = new Mul(x, num);

        // производная x*5 по x = 1*5 + x*0 = 5
        Expression derivative = mul.derivative("x");
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(5, derivative.eval(values));
    }

    @Test
    void testDerivativeComplex() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");
        Mul mul = new Mul(x, y);

        // производная x*y по x = 1*y + x*0 = y
        Expression derivative = mul.derivative("x");
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 7);
        values.put("y", 3);
        assertEquals(3, derivative.eval(values));
    }

    @Test
    void testToString() {
        Expression left = new Number(3);
        Expression right = new Number(4);
        Mul mul = new Mul(left, right);

        assertEquals("(3 * 4)", mul.toString());
    }

    @Test
    void testEquals() {
        Expression a1 = new Number(2);
        Expression b1 = new Number(3);
        Mul mul1 = new Mul(a1, b1);

        Expression a2 = new Number(2);
        Expression b2 = new Number(3);
        Mul mul2 = new Mul(a2, b2);

        Expression a3 = new Number(3);
        Expression b3 = new Number(2);
        Mul mul3 = new Mul(a3, b3);

        Expression a4 = new Number(3);
        Expression b4 = new Number(4);
        Mul mul4 = new Mul(a4, b4);

        assertTrue(mul1.equals(mul2)); // Одинаковые выражения
        assertTrue(mul1.equals(mul3)); // Коммутативность умножения
        assertFalse(mul1.equals(mul4)); // Разные выражения
        assertFalse(mul1.equals(new Add(a1, b1))); // Разные типы операций
    }

    @Test
    void testHashCode() {
        Expression a1 = new Number(5);
        Expression b1 = new Number(6);
        Mul mul1 = new Mul(a1, b1);

        Expression a2 = new Number(5);
        Expression b2 = new Number(6);
        Mul mul2 = new Mul(a2, b2);

        // Одинаковые выражения должны иметь одинаковые хэш-коды
        assertEquals(mul1.hashCode(), mul2.hashCode());
    }

    @Test
    void testSimplify() {
        Expression a = new Number(4);
        Expression b = new Number(5);
        Mul mul = new Mul(a, b);
        Expression simplified = mul.simplify();
        assertTrue(simplified instanceof Number);
        assertEquals(20, simplified.eval(new HashMap<>()));

        Expression x = new Variable("x");
        Expression num = new Number(5);
        Mul mulWithVar = new Mul(x, num);
        Expression simplifiedWithVar = mulWithVar.simplify();
        assertTrue(simplifiedWithVar instanceof Mul);
        assertEquals("(x * 5)", simplifiedWithVar.toString());

        Expression zero = new Number(0);
        Mul mulByZero = new Mul(x, zero);
        Expression simplifiedByZero = mulByZero.simplify();
        assertTrue(simplifiedByZero instanceof Number);
        assertEquals(0, simplifiedByZero.eval(new HashMap<>()));

        Expression one = new Number(1);
        Mul mulByOne = new Mul(x, one);
        Expression simplifiedByOne = mulByOne.simplify();
        assertEquals(x, simplifiedByOne);

        Expression innerMul = new Mul(new Number(3), new Number(4));
        Mul outerMul = new Mul(innerMul, new Number(2));
        Expression simplifiedComplex = outerMul.simplify();
        assertTrue(simplifiedComplex instanceof Number);
        assertEquals(24, simplifiedComplex.eval(new HashMap<>())); // (3*4)*2 = 12*2 = 24
    }
}
