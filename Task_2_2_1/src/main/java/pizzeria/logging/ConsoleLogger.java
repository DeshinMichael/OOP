package pizzeria.logging;

public class ConsoleLogger implements PizzeriaLogger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
