package operation_with_equations.operations;

import operation_with_equations.model.BinaryOperation;
import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;

import java.util.HashMap;
import java.util.Map;

public class Div extends BinaryOperation {
    public Div(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Expression derivative(String var) {
        return new Div(
                new Sub(
                        new Mul(getLeftOperand().derivative(var), getRightOperand()),
                        new Mul(getLeftOperand(), getRightOperand().derivative(var))
                ),
                new Mul(getRightOperand(), getRightOperand())
        );
    }

    @Override
    public int eval(Map<String, Integer> values) {
        int divisor = getRightOperand().eval(values);
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero ");
        }
        return getLeftOperand().eval(values) / divisor;
    }

    @Override
    public Expression simplify() {
        Expression left = getLeftOperand().simplify();
        Expression right = getRightOperand().simplify();

        if (left instanceof Number && right instanceof Number) {
            Map<String, Integer> emptyMap = new HashMap<>();
            int divisor = ((Number) right).eval(emptyMap);
            if (divisor != 0) {
                return new Number(((Number) left).eval(emptyMap) / divisor);
            }
        }

        if (left instanceof Number && ((Number) left).eval(new HashMap<>()) == 0) {
            return new Number(0);
        }

        if (right instanceof Number && ((Number) right).eval(new HashMap<>()) == 1) {
            return left;
        }

        return new Div(left, right);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Div)) return false;
        Div other = (Div) obj;
        return getLeftOperand().equals(other.getLeftOperand()) && getRightOperand().equals(other.getRightOperand());
    }

    @Override
    public int hashCode() {
        return getLeftOperand().hashCode() / (getRightOperand().hashCode() != 0 ? getRightOperand().hashCode() : 1);
    }

    @Override
    public String toString() {
        return "(" + getLeftOperand() + "/" + getRightOperand() + ")";
    }
}