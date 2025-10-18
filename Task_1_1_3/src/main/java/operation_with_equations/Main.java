package operation_with_equations;

import operation_with_equations.exceptions.ParseException;
import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import operation_with_equations.operations.Add;
import operation_with_equations.operations.Mul;
import operation_with_equations.parser.AdvancedExpressionParser;
import operation_with_equations.parser.ExpressionParser;

public class Main {
    public static void main(String[] args) {

        Expression parsed = null;
        try {
            parsed = ExpressionParser.parse("(3+(2*x))");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
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

        Expression simplifiedExpr1 = null;
        try {
            simplifiedExpr1 = AdvancedExpressionParser.parseAndSimplify("(1 * x)");
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Simplified expression: " + simplifiedExpr1);

        Expression simplifiedExpr2 = null;
        try {
            simplifiedExpr2 = AdvancedExpressionParser.parseAndSimplify("x - x");
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("Simplified expression: " + simplifiedExpr2);
    }
}