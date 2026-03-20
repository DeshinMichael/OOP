package pizzeria.lifecycle;

import pizzeria.config.PizzeriaConfig;
import pizzeria.core.OrderQueue;
import pizzeria.core.Storage;
import pizzeria.factory.WorkerFactory;
import pizzeria.generator.OrderGenerator;
import pizzeria.logging.ConsoleLogger;
import pizzeria.logging.OrderLogger;
import pizzeria.logging.PizzeriaLogger;

import java.util.List;

public class Pizzeria implements PizzeriaLifecycle {
    private final PizzeriaConfig config;
    private final OrderQueue orderQueue;
    private final Storage storage;
    private final WorkerFactory workerFactory;
    private final PizzeriaLogger logger;
    private final ShutdownStrategy shutdownStrategy;

    public Pizzeria(PizzeriaConfig config) {
        this.config = config;
        this.logger = new ConsoleLogger();
        this.orderQueue = new OrderQueue();
        this.storage = new Storage(config.getStorageCapacity());
        this.workerFactory = new WorkerFactory(orderQueue, storage, logger);
        this.shutdownStrategy = new GracefulShutdownStrategy(logger);
    }

    public Pizzeria(PizzeriaConfig config, ShutdownStrategy shutdownStrategy) {
        this.config = config;
        this.logger = new ConsoleLogger();
        this.orderQueue = new OrderQueue();
        this.storage = new Storage(config.getStorageCapacity());
        this.workerFactory = new WorkerFactory(orderQueue, storage, logger);
        this.shutdownStrategy = shutdownStrategy;
    }

    @Override
    public void start() throws InterruptedException {
        List<Thread> bakerThreads = startWorkers(workerFactory.createBakers(config.getBakers()));
        List<Thread> courierThreads = startWorkers(workerFactory.createCouriers(config.getCouriers()));

        Thread generatorThread = startOrderGenerator();

        Thread.sleep(config.getWorkingTimeMs());

        shutdownStrategy.shutdown(generatorThread, bakerThreads, courierThreads, orderQueue, storage);
    }

    private Thread startOrderGenerator() {
        OrderGenerator generator = new OrderGenerator(
                config.getTotalOrders(), config.getOrderIntervalMs(),
                orderQueue, new OrderLogger(logger), logger
        );
        Thread t = new Thread(generator, "OrderGenerator");
        t.start();
        return t;
    }

    private List<Thread> startWorkers(List<Thread> threads) {
        threads.forEach(Thread::start);
        return threads;
    }
}
