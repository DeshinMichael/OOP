package operation_with_equations.parser;

import operation_with_equations.exceptions.ParseException;
import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import operation_with_equations.operations.Add;
import operation_with_equations.operations.Div;
import operation_with_equations.operations.Mul;
import operation_with_equations.operations.Sub;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class ExpressionParserTest {

    @Test
    void testParseNumber() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("42");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Number);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(42, expr.eval(emptyMap));
    }

    @Test
    void testParseVariable() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("x");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Variable);
        assertEquals("x", expr.toString());

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(10, expr.eval(values));
    }

    @Test
    void testParseAddition() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("5+3");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Add);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(8, expr.eval(emptyMap));
    }

    @Test
    void testParseSubtraction() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("10-4");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Sub);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(6, expr.eval(emptyMap));
    }

    @Test
    void testParseMultiplication() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("6*7");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Mul);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(42, expr.eval(emptyMap));
    }

    @Test
    void testParseDivision() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("20/5");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Div);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(4, expr.eval(emptyMap));
    }

    @Test
    void testParseParenthesizedExpression() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("(3+2)");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(5, expr.eval(emptyMap));
    }

    @Test
    void testParseComplexExpression() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("2*(3+4)");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(14, expr.eval(emptyMap));
    }

    @Test
    void testParseNestedExpression() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("(2+3)*(4-1)");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(15, expr.eval(emptyMap));
    }

    @Test
    void testParsePrecedence() {
        // Проверка приоритета операций: умножение перед сложением
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("2+3*4");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(14, expr.eval(emptyMap));
    }

    @Test
    void testParseWithWhitespace() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse(" 2 + 3 ");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(5, expr.eval(emptyMap));
    }

    @Test
    void testParseInvalidExpression() {
        assertThrows(ParseException.class, () -> {
            ExpressionParser.parse("2+");
        });
    }

    @Test
    void testParseUnbalancedParentheses() {
        assertThrows(ParseException.class, () -> {
            ExpressionParser.parse("(2+3");
        });
    }

    @Test
    void testParseDerivative() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parse("x+5");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Expression derivative = expr.derivative("x");
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(1, derivative.eval(emptyMap)); // Производная x+5 по x равна 1
    }

    @Test
    void testParseAndSimplify() {
        Expression expr = null;
        try {
            expr = ExpressionParser.parseAndSimplify("2+3");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Number);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(5, expr.eval(emptyMap));

        try {
            expr = ExpressionParser.parseAndSimplify("(2+3)*(4-1)");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Number);
        assertEquals(15, expr.eval(emptyMap));

        try {
            expr = ExpressionParser.parseAndSimplify("x+5");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Add);
        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(15, expr.eval(values));

        try {
            expr = ExpressionParser.parseAndSimplify("(2+3)*x");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Mul);
        assertEquals("(5 * x)", expr.toString());
        values.put("x", 4);
        assertEquals(20, expr.eval(values));

        try {
            expr = ExpressionParser.parseAndSimplify("10/2");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Number);
        assertEquals(5, expr.eval(emptyMap));

        try {
            expr = ExpressionParser.parseAndSimplify("x*0");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Number);
        assertEquals(0, expr.eval(emptyMap));

        try {
            expr = ExpressionParser.parseAndSimplify("x*1");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertTrue(expr instanceof Variable);
        assertEquals("x", expr.toString());
    }
}
