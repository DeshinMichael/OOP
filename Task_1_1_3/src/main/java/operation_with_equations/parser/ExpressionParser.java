package operation_with_equations.parser;

import operation_with_equations.exceptions.ParseException;
import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import operation_with_equations.operations.Add;
import operation_with_equations.operations.Div;
import operation_with_equations.operations.Mul;
import operation_with_equations.operations.Sub;

public class ExpressionParser {

    public static Expression parse(String input) throws ParseException {
        input = input.trim();
        return parseExpression(input);
    }

    public static Expression parseAndSimplify(String input) throws ParseException {
        Expression expr = parse(input);
        return expr.simplify();
    }

    private static Expression parseExpression(String expr) throws ParseException {
        expr = expr.trim();

        try {
            return new Number(Integer.parseInt(expr));
        } catch (NumberFormatException ignored) {}

        if (isVariable(expr)) {
            return new Variable(expr);
        }

        if (expr.startsWith("(") && expr.endsWith(")") && isBalancedSubstring(expr, 1, expr.length() - 1)) {
            expr = expr.substring(1, expr.length() - 1);
        }

        int depth = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;

            if (depth == 0) {
                if (c == '+') {
                    return new Add(parseExpression(expr.substring(0, i)),
                            parseExpression(expr.substring(i + 1)));
                } else if (c == '-') {
                    return new Sub(parseExpression(expr.substring(0, i)),
                            parseExpression(expr.substring(i + 1)));
                }
            }
        }

        depth = 0;
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;

            if (depth == 0) {
                if (c == '*') {
                    return new Mul(parseExpression(expr.substring(0, i)),
                            parseExpression(expr.substring(i + 1)));
                } else if (c == '/') {
                    return new Div(parseExpression(expr.substring(0, i)),
                            parseExpression(expr.substring(i + 1)));
                }
            }
        }

        if (expr.startsWith("(") && expr.endsWith(")")) {
            return parseExpression(expr.substring(1, expr.length() - 1));
        }

        throw new ParseException("It is impossible to make out the expression: " + expr);
    }

    private static boolean isVariable(String str) {
        if (str.isEmpty()) return false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }

        return !Character.isDigit(str.charAt(0));
    }

    private static boolean isBalancedSubstring(String str, int start, int end) {
        int depth = 0;
        for (int i = start; i < end; i++) {
            char c = str.charAt(i);
            if (c == '(') depth++;
            else if (c == ')') depth--;
            if (depth < 0) return false;
        }
        return depth == 0;
    }
}
