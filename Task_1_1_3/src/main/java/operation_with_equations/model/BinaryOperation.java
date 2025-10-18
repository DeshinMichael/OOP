package operation_with_equations.model;

public abstract class BinaryOperation extends Expression {
    private final Expression leftOperand;
    private final Expression rightOperand;

    public BinaryOperation(Expression leftOperand, Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    protected Expression getLeftOperand() {
        return leftOperand;
    }

    protected Expression getRightOperand() {
        return rightOperand;
    }
}