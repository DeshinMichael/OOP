package operation_with_equations.operations;

import operation_with_equations.model.BinaryOperation;
import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;

import java.util.Map;

public class Sub extends BinaryOperation {

    public Sub(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(getLeftOperand().derivative(var), getRightOperand().derivative(var));
    }

    @Override
    public int eval(java.util.Map<String, Integer> values) {
        return getLeftOperand().eval(values) - getRightOperand().eval(values);
    }

    @Override
    public Expression simplify() {
        Expression left = getLeftOperand().simplify();
        Expression right = getRightOperand().simplify();

        if (left instanceof Number && right instanceof Number) {
            Map<String, Integer> emptyMap = new java.util.HashMap<>();
            return new Number(((Number) left).eval(emptyMap) - ((Number) right).eval(emptyMap));
        }

        if (left.equals(right)) {
            return new Number(0);
        }

        return new Sub(left, right);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Sub)) return false;
        Sub other = (Sub) obj;
        return getLeftOperand().equals(other.getLeftOperand()) && getRightOperand().equals(other.getRightOperand());
    }

    @Override
    public int hashCode() {
        return getLeftOperand().hashCode() - getRightOperand().hashCode();
    }

    @Override
    public String toString() {
        return "(" + getLeftOperand().toString() + " - " + getRightOperand().toString() + ")";
    }
}