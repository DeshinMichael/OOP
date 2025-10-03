package operation_with_equations.parser;

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
        Expression expr = ExpressionParser.parse("42");
        assertTrue(expr instanceof Number);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(42, expr.eval(emptyMap));
    }

    @Test
    void testParseVariable() {
        Expression expr = ExpressionParser.parse("x");
        assertTrue(expr instanceof Variable);
        assertEquals("x", expr.toString());

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(10, expr.eval(values));
    }

    @Test
    void testParseAddition() {
        Expression expr = ExpressionParser.parse("5+3");
        assertTrue(expr instanceof Add);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(8, expr.eval(emptyMap));
    }

    @Test
    void testParseSubtraction() {
        Expression expr = ExpressionParser.parse("10-4");
        assertTrue(expr instanceof Sub);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(6, expr.eval(emptyMap));
    }

    @Test
    void testParseMultiplication() {
        Expression expr = ExpressionParser.parse("6*7");
        assertTrue(expr instanceof Mul);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(42, expr.eval(emptyMap));
    }

    @Test
    void testParseDivision() {
        Expression expr = ExpressionParser.parse("20/5");
        assertTrue(expr instanceof Div);
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(4, expr.eval(emptyMap));
    }

    @Test
    void testParseParenthesizedExpression() {
        Expression expr = ExpressionParser.parse("(3+2)");
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(5, expr.eval(emptyMap));
    }

    @Test
    void testParseComplexExpression() {
        Expression expr = ExpressionParser.parse("2*(3+4)");
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(14, expr.eval(emptyMap));
    }

    @Test
    void testParseNestedExpression() {
        Expression expr = ExpressionParser.parse("(2+3)*(4-1)");
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(15, expr.eval(emptyMap));
    }

    @Test
    void testParsePrecedence() {
        // Проверка приоритета операций: умножение перед сложением
        Expression expr = ExpressionParser.parse("2+3*4");
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(14, expr.eval(emptyMap));
    }

    @Test
    void testParseWithWhitespace() {
        Expression expr = ExpressionParser.parse(" 2 + 3 ");
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(5, expr.eval(emptyMap));
    }

    @Test
    void testParseInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionParser.parse("2+");
        });
    }

    @Test
    void testParseUnbalancedParentheses() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionParser.parse("(2+3");
        });
    }

    @Test
    void testParseDerivative() {
        Expression expr = ExpressionParser.parse("x+5");
        Expression derivative = expr.derivative("x");
        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(1, derivative.eval(emptyMap)); // Производная x+5 по x равна 1
    }
}
