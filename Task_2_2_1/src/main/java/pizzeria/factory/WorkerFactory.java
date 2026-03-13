package pizzeria.factory;

import pizzeria.config.BakerConfig;
import pizzeria.config.CourierConfig;
import pizzeria.core.OrderQueue;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;
import pizzeria.workers.Baker;
import pizzeria.workers.Courier;

import java.util.ArrayList;
import java.util.List;

public class WorkerFactory {
    private final OrderQueue orderQueue;
    private final Storage storage;
    private final PizzeriaLogger logger;

    public WorkerFactory(OrderQueue orderQueue, Storage storage, PizzeriaLogger logger) {
        this.orderQueue = orderQueue;
        this.storage = storage;
        this.logger = logger;
    }

    public List<Thread> createBakers(List<BakerConfig> configs) {
        List<Thread> threads = new ArrayList<>();
        for (BakerConfig bc : configs) {
            Baker baker = new Baker(bc.getId(), bc.getCookingTimeMs(), orderQueue, storage, logger);
            threads.add(new Thread(baker, "Baker-" + bc.getId()));
        }
        return threads;
    }

    public List<Thread> createCouriers(List<CourierConfig> configs) {
        List<Thread> threads = new ArrayList<>();
        for (CourierConfig cc : configs) {
            Courier courier = new Courier(cc.getId(), cc.getTrunkSize(), cc.getDeliveryTimeMs(), storage, logger);
            threads.add(new Thread(courier, "Courier-" + cc.getId()));
        }
        return threads;
    }
}
