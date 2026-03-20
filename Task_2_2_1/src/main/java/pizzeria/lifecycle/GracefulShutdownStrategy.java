package pizzeria.lifecycle;

import pizzeria.core.OrderQueue;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;

import java.util.List;

public class GracefulShutdownStrategy implements ShutdownStrategy {
    private final PizzeriaLogger logger;

    public GracefulShutdownStrategy(PizzeriaLogger logger) {
        this.logger = logger;
    }

    @Override
    public void shutdown(Thread generatorThread,
                         List<Thread> bakerThreads,
                         List<Thread> courierThreads,
                         OrderQueue orderQueue,
                         Storage storage) throws InterruptedException {
        logger.log("\n=== Pizzeria shutdown initiated ===");

        generatorThread.interrupt();
        orderQueue.stopAccepting();
        logger.log("Order queue closed. Waiting for bakers to finish remaining orders...");

        joinAll(bakerThreads);
        logger.log("All bakers finished.");

        storage.close();
        logger.log("Storage closed. Waiting for couriers to deliver remaining pizzas...");

        joinAll(courierThreads);
        logger.log("All couriers finished.");
        logger.log("=== Pizzeria closed ===");
    }

    private void joinAll(List<Thread> threads) throws InterruptedException {
        for (Thread t : threads) t.join();
    }
}
