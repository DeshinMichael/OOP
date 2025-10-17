package operation_with_equations.parser;

import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import operation_with_equations.operations.Add;
import operation_with_equations.operations.Div;
import operation_with_equations.operations.Mul;
import operation_with_equations.operations.Sub;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedExpressionParserTest {

    @Test
    void testParseNumber() {
        Expression result = AdvancedExpressionParser.parse("42");
        assertTrue(result instanceof Number);
        assertEquals(42, result.eval(new HashMap<>()));
    }

    @Test
    void testParseVariable() {
        Expression result = AdvancedExpressionParser.parse("x");
        assertTrue(result instanceof Variable);
        assertEquals("x", result.toString());

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 10);
        assertEquals(10, result.eval(values));
    }

    @Test
    void testParseAddition() {
        Expression result = AdvancedExpressionParser.parse("5 + 3");
        assertTrue(result instanceof Add);
        assertEquals("(5 + 3)", result.toString());
        assertEquals(8, result.eval(new HashMap<>()));
    }

    @Test
    void testParseSubtraction() {
        Expression result = AdvancedExpressionParser.parse("10 - 4");
        assertTrue(result instanceof Sub);
        assertEquals("(10 - 4)", result.toString());
        assertEquals(6, result.eval(new HashMap<>()));
    }

    @Test
    void testParseMultiplication() {
        Expression result = AdvancedExpressionParser.parse("7 * 3");
        assertTrue(result instanceof Mul);
        assertEquals("(7 * 3)", result.toString());
        assertEquals(21, result.eval(new HashMap<>()));
    }

    @Test
    void testParseDivision() {
        Expression result = AdvancedExpressionParser.parse("15 / 5");
        assertTrue(result instanceof Div);
        assertEquals("(15/5)", result.toString());
        assertEquals(3, result.eval(new HashMap<>()));
    }

    @Test
    void testParseParentheses() {
        Expression result = AdvancedExpressionParser.parse("(3 + 4) * 2");
        assertTrue(result instanceof Mul);
        assertEquals("((3 + 4) * 2)", result.toString());
        assertEquals(14, result.eval(new HashMap<>()));
    }

    @Test
    void testParseNestedParentheses() {
        Expression result = AdvancedExpressionParser.parse("(2 + (3 * 4)) / 2");
        assertEquals(7, result.eval(new HashMap<>()));
    }

    @Test
    void testParseComplexExpression() {
        Expression result = AdvancedExpressionParser.parse("x * (y + 5) - 10 / z");

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 3);
        values.put("y", 4);
        values.put("z", 2);

        assertEquals(22, result.eval(values)); // 3 * (4 + 5) - 10 / 2 = 3 * 9 - 5 = 27 - 5 = 22
    }

    @Test
    void testOperatorPrecedence() {
        Expression result = AdvancedExpressionParser.parse("2 + 3 * 4");
        assertEquals(14, result.eval(new HashMap<>())); // 2 + (3 * 4) = 2 + 12 = 14

        result = AdvancedExpressionParser.parse("10 - 6 / 2");
        assertEquals(7, result.eval(new HashMap<>())); // 10 - (6 / 2) = 10 - 3 = 7
    }

    @Test
    void testWhitespaceHandling() {
        Expression result = AdvancedExpressionParser.parse("  5+  3 ");
        assertEquals(8, result.eval(new HashMap<>()));

        result = AdvancedExpressionParser.parse("7*3");
        assertEquals(21, result.eval(new HashMap<>()));
    }

    @Test
    void testExceptionUnexpectedEnd() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AdvancedExpressionParser.parse("5 + ")
        );
        assertEquals("Unexpected end of an expression", exception.getMessage());
    }

    @Test
    void testExceptionMissingClosingParenthesis() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AdvancedExpressionParser.parse("(5 + 3")
        );
        assertEquals("Closing parenthesis was expected", exception.getMessage());
    }

    @Test
    void testParseAndSimplify() {
        Expression result = AdvancedExpressionParser.parseAndSimplify("3 + 4");
        assertTrue(result instanceof Number);
        assertEquals(7, result.eval(new HashMap<>()));

        result = AdvancedExpressionParser.parseAndSimplify("x * (2 + 3)");
        assertTrue(result instanceof Mul);

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 4);
        assertEquals(20, result.eval(values));
    }

    @Test
    void testParseMultipleOperations() {
        Expression result = AdvancedExpressionParser.parse("1 + 2 + 3 + 4");
        assertEquals(10, result.eval(new HashMap<>()));

        result = AdvancedExpressionParser.parse("10 - 5 - 2");
        assertEquals(3, result.eval(new HashMap<>()));
    }
}
