package pizzeria.lifecycle;

import pizzeria.core.OrderQueue;
import pizzeria.core.Storage;

import java.util.List;

public interface ShutdownStrategy {
    void shutdown(Thread generatorThread,
                  List<Thread> bakerThreads,
                  List<Thread> courierThreads,
                  OrderQueue orderQueue,
                  Storage storage) throws InterruptedException;
}
