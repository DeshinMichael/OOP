package operation_with_equations.operations;

import operation_with_equations.model.BinaryOperation;
import operation_with_equations.model.Expression;

import java.util.Map;

public class Add extends BinaryOperation {

    public Add(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(getLeftOperand().derivative(var), getRightOperand().derivative(var));
    }

    @Override
    public int eval(Map<String, Integer> values) {
        return getLeftOperand().eval(values) + getRightOperand().eval(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Add)) return false;
        Add other = (Add) obj;
        return (getLeftOperand().equals(other.getLeftOperand()) && getRightOperand().equals(other.getRightOperand())) ||
                (getLeftOperand().equals(other.getRightOperand()) && getRightOperand().equals(other.getLeftOperand()));
    }

    @Override
    public int hashCode() {
        return getLeftOperand().hashCode() + getRightOperand().hashCode();
    }

    @Override
    public String toString() {
        return "(" + getLeftOperand().toString() + " + " + getRightOperand().toString() + ")";
    }
}