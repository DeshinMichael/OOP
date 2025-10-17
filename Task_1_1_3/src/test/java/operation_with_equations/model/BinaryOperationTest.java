package operation_with_equations.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class BinaryOperationTest {

    private static class TestBinaryOperation extends BinaryOperation {

        public TestBinaryOperation(Expression left, Expression right) {
            super(left, right);
        }

        @Override
        public Expression derivative(String var) {
            return new Number(1); // Тестовая реализация
        }

        @Override
        public int eval(Map<String, Integer> values) {
            return getLeftOperand().eval(values) + getRightOperand().eval(values);
        }

        @Override
        public Expression simplify() {
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestBinaryOperation)) return false;
            TestBinaryOperation other = (TestBinaryOperation) obj;
            return getLeftOperand().equals(other.getLeftOperand()) &&
                   getRightOperand().equals(other.getRightOperand());
        }

        @Override
        public int hashCode() {
            return getLeftOperand().hashCode() * 31 + getRightOperand().hashCode();
        }

        @Override
        public String toString() {
            return "(" + getLeftOperand() + " # " + getRightOperand() + ")";
        }
    }

    @Test
    void testGetLeftOperand() {
        Expression left = new Number(5);
        Expression right = new Number(10);
        TestBinaryOperation operation = new TestBinaryOperation(left, right);

        assertEquals(left, operation.getLeftOperand());
    }

    @Test
    void testGetRightOperand() {
        Expression left = new Number(5);
        Expression right = new Number(10);
        TestBinaryOperation operation = new TestBinaryOperation(left, right);

        assertEquals(right, operation.getRightOperand());
    }

    @Test
    void testEval() {
        Expression left = new Number(5);
        Expression right = new Number(10);
        TestBinaryOperation operation = new TestBinaryOperation(left, right);

        assertEquals(15, operation.eval(new HashMap<>()));
    }

    @Test
    void testEvalWithVariables() {
        Expression left = new Variable("x");
        Expression right = new Variable("y");
        TestBinaryOperation operation = new TestBinaryOperation(left, right);

        Map<String, Integer> values = new HashMap<>();
        values.put("x", 20);
        values.put("y", 30);

        assertEquals(50, operation.eval(values));
    }

    @Test
    void testDerivative() {
        Expression left = new Variable("x");
        Expression right = new Number(10);
        TestBinaryOperation operation = new TestBinaryOperation(left, right);

        Map<String, Integer> emptyMap = new HashMap<>();
        assertEquals(1, operation.derivative("x").eval(emptyMap));
    }
}
