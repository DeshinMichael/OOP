package operation_with_equations.operations;

import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {

    @Test
    void derivative() {
        Expression x = new Variable("x");
        Expression num = new Number(5);
        Expression div = new Div(x, num);
        Expression derivative = div.derivative("x");
        assertEquals("(((1 * 5) - (x * 0))/(5 * 5))", derivative.toString());
    }

    @Test
    void eval() {
        Expression x = new Variable("x");
        Expression num = new Number(5);
        Expression div = new Div(x, num);
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(2, div.eval(values));
    }

    @Test
    void evalDivisionByZero() {
        Expression x = new Variable("x");
        Expression num = new Number(10);
        Expression div = new Div(num, x);
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 0);
        assertThrows(ArithmeticException.class, () -> div.eval(values));
    }

    @Test
    void testEquals() {
        Expression x = new Variable("x");
        Expression num5 = new Number(5);
        Expression num10 = new Number(10);

        Div div1 = new Div(x, num5);
        Div div2 = new Div(x, num5);
        Div div3 = new Div(num5, x);
        Div div4 = new Div(x, num10);

        assertEquals(div1, div2);
        assertNotEquals(div1, div3);
        assertNotEquals(div1, div4);
        assertNotEquals(div1, new Object());
        assertNotEquals(div1, null);
    }

    @Test
    void testHashCode() {
        Expression x = new Variable("x");
        Expression num5 = new Number(5);
        Expression num0 = new Number(0);

        Div div1 = new Div(x, num5);
        Div div2 = new Div(x, num5);
        Div div3 = new Div(x, num0);

        assertEquals(div1.hashCode(), div2.hashCode());
        assertDoesNotThrow(div3::hashCode);
    }

    @Test
    void testToString() {
        Expression x = new Variable("x");
        Expression num = new Number(5);
        Expression div = new Div(x, num);
        assertEquals("(x/5)", div.toString());
    }
}

