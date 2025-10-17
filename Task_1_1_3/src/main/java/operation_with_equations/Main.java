package operation_with_equations;

import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import operation_with_equations.operations.Add;
import operation_with_equations.operations.Mul;
import operation_with_equations.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {

        Expression parsed = ExpressionParser.parse("(3+(2*x))");
        System.out.print("Parsed expression: ");
        parsed.print();

        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        System.out.print("Expression: ");
        e.print();

        Expression de = e.derivative("x");
        System.out.print("Derivative of x: ");
        de.print();

        int result = e.eval("x = 10; y = 13");
        System.out.println("Value at x=10: " + result);

        Expression simplifiedExpr1 = ExpressionParser.parseAndSimplify("(1 * x)");
        System.out.println("Simplified expression: " + simplifiedExpr1);

        Expression simplifiedExpr2 = ExpressionParser.parseAndSimplify("x - x");
        System.out.println("Simplified expression: " + simplifiedExpr2);
    }
}