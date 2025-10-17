package operation_with_equations.parser;

import operation_with_equations.model.Expression;
import operation_with_equations.model.Number;
import operation_with_equations.model.Variable;
import operation_with_equations.operations.Add;
import operation_with_equations.operations.Div;
import operation_with_equations.operations.Mul;
import operation_with_equations.operations.Sub;

public class AdvancedExpressionParser {
    private static String input;
    private static int pos;

    public static Expression parse(String expression) {
        input = expression.trim();
        pos = 0;
        return parseAddSub();
    }

    public static Expression parseAndSimplify(String input) {
        Expression expr = parse(input);
        return expr.simplify();
    }

    private static Expression parseAddSub() {
        Expression left = parseMulDiv();

        while (pos < input.length()) {
            skipWhitespace();
            char op = input.charAt(pos);
            if (op != '+' && op != '-') break;
            pos++;

            skipWhitespace();
            Expression right = parseMulDiv();

            if (op == '+') {
                left = new Add(left, right);
            } else {
                left = new Sub(left, right);
            }
        }

        return left;
    }

    private static Expression parseMulDiv() {
        Expression left = parsePrimary();

        while (pos < input.length()) {
            skipWhitespace();
            char op = input.charAt(pos);
            if (op != '*' && op != '/') break;
            pos++;

            skipWhitespace();
            Expression right = parsePrimary();

            if (op == '*') {
                left = new Mul(left, right);
            } else {
                left = new Div(left, right);
            }
        }

        return left;
    }

    private static Expression parsePrimary() {
        skipWhitespace();

        if (pos >= input.length()) {
            throw new IllegalArgumentException("Unexpected end of an expression");
        }

        char c = input.charAt(pos);

        if (c == '(') {
            pos++;
            Expression result = parseAddSub();
            if (pos >= input.length() || input.charAt(pos) != ')') {
                throw new IllegalArgumentException("Closing parenthesis was expected");
            }
            pos++;
            return result;
        }

        if (Character.isDigit(c)) {
            int start = pos;
            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                pos++;
            }
            int value = Integer.parseInt(input.substring(start, pos));
            return new Number(value);
        }

        if (Character.isLetter(c)) {
            int start = pos;
            while (pos < input.length() &&
                    (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
                pos++;
            }
            String name = input.substring(start, pos);
            return new Variable(name);
        }

        throw new IllegalArgumentException("Unexpected symbol: " + c);
    }

    private static void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }
}
