package operation_with_equations.operations;

import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AddTest {

    private final Expression five = new Number(5);
    private final Expression two = new Number(2);
    private final Expression x = new Variable("x");

    @Test
    void derivative() {
        Add add = new Add(x, five);
        Expression expected = new Add(new Number(1), new Number(0));
        assertEquals(expected, add.derivative("x"));
    }

    @Test
    void eval() {
        Add add = new Add(five, two);
        assertEquals(7, add.eval(Collections.emptyMap()));

        Add addWithVar = new Add(x, five);
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(15, addWithVar.eval(values));
    }

    @Test
    void testEquals() {
        Add add1 = new Add(five, x);
        Add add2 = new Add(x, five);
        Add add3 = new Add(five, x);
        Add add4 = new Add(two, x);

        assertEquals(add1, add2); // Commutative
        assertEquals(add1, add3);
        assertNotEquals(add1, add4);
        assertNotEquals(add1, new Object());
        assertNotEquals(null, add1);
        assertEquals(add1, add1);
    }

    @Test
    void testHashCode() {
        Add add1 = new Add(five, x);
        Add add2 = new Add(x, five);
        assertEquals(add1.hashCode(), add2.hashCode());
    }

    @Test
    void testToString() {
        Add add = new Add(five, x);
        assertEquals("(5 + x)", add.toString());
    }
}

