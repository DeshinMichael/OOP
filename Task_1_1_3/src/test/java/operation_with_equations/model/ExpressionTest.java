package operation_with_equations.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

class ExpressionTest {
    private TestExpression expression;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        expression = new TestExpression();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testEvalWithAssignmentsString() {
        expression.returnValue = 42;
        assertEquals(42, expression.eval("x=10; y=20"));
    }

    @Test
    void testEvalWithEmptyAssignments() {
        expression.returnValue = 15;
        assertEquals(15, expression.eval(""));
    }

    @Test
    void testEvalWithNullAssignments() {
        expression.returnValue = 30;
        assertEquals(30, expression.eval((Map<String, Integer>) null));
    }

    // Вспомогательный класс для тестирования абстрактных методов Expression
    private static class TestExpression extends Expression {
        public int returnValue;
        public String toString = "";

        @Override
        public Expression derivative(String var) {
            return new Number(0);
        }

        @Override
        public int eval(Map<String, Integer> values) {
            return returnValue;
        }

        @Override
        public Expression simplify() {
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestExpression;
        }

        @Override
        public int hashCode() {
            return 42;
        }

        @Override
        public String toString() {
            return toString;
        }
    }
}
