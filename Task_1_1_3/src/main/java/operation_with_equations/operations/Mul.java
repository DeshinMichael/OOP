package operation_with_equations.operations;

import operation_with_equations.model.BinaryOperation;
import operation_with_equations.model.Expression;

public class Mul extends BinaryOperation {

    public Mul(Expression leftOperand, Expression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(
            new Mul(getLeftOperand().derivative(var), getRightOperand()),
            new Mul(getLeftOperand(), getRightOperand().derivative(var))
        );
    }

    @Override
    public int eval(java.util.Map<String, Integer> values) {
        return getLeftOperand().eval(values) * getRightOperand().eval(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mul)) return false;
        Mul other = (Mul) obj;
        return (getLeftOperand().equals(other.getLeftOperand()) && getRightOperand().equals(other.getRightOperand())) ||
               (getLeftOperand().equals(other.getRightOperand()) && getRightOperand().equals(other.getLeftOperand()));
    }

    @Override
    public int hashCode() {
        return getLeftOperand().hashCode() * getRightOperand().hashCode();
    }

    @Override
    public String toString() {
        return "(" + getLeftOperand().toString() + " * " + getRightOperand().toString() + ")";
    }
}