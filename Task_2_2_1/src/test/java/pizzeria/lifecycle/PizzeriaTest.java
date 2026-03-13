package pizzeria.lifecycle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pizzeria.config.PizzeriaConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class PizzeriaTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private PizzeriaConfig buildConfig(int totalOrders, int workingTimeMs) throws Exception {
        String json = """
                {
                  "storageCapacity": 5,
                  "workingTimeMs": %d,
                  "orderIntervalMs": 0,
                  "totalOrders": %d,
                  "bakers": [
                    {"id": 1, "cookingTimeMs": 50}
                  ],
                  "couriers": [
                    {"id": 1, "trunkSize": 3, "deliveryTimeMs": 50}
                  ]
                }
                """.formatted(workingTimeMs, totalOrders);
        return mapper.readValue(json, PizzeriaConfig.class);
    }

    @Test
    void startRunsWithoutException() throws Exception {
        PizzeriaConfig config = buildConfig(2, 500);
        Pizzeria pizzeria = new Pizzeria(config);
        assertDoesNotThrow(pizzeria::start);
    }

    @Test
    void startCallsShutdownStrategy() throws Exception {
        PizzeriaConfig config = buildConfig(1, 100);
        AtomicBoolean shutdownCalled = new AtomicBoolean(false);

        ShutdownStrategy strategy = (generatorThread, bakerThreads, courierThreads, orderQueue, storage) -> {
            shutdownCalled.set(true);
            generatorThread.interrupt();
            orderQueue.stopAccepting();
            for (Thread t : bakerThreads) t.join();
            storage.close();
            for (Thread t : courierThreads) t.join();
        };

        Pizzeria pizzeria = new Pizzeria(config, strategy);
        pizzeria.start();

        assertTrue(shutdownCalled.get());
    }

    @Test
    void startPassesCorrectThreadCountsToShutdown() throws Exception {
        String json = """
                {
                  "storageCapacity": 5,
                  "workingTimeMs": 100,
                  "orderIntervalMs": 0,
                  "totalOrders": 1,
                  "bakers": [
                    {"id": 1, "cookingTimeMs": 50},
                    {"id": 2, "cookingTimeMs": 50}
                  ],
                  "couriers": [
                    {"id": 1, "trunkSize": 3, "deliveryTimeMs": 50},
                    {"id": 2, "trunkSize": 2, "deliveryTimeMs": 50},
                    {"id": 3, "trunkSize": 1, "deliveryTimeMs": 50}
                  ]
                }
                """;
        PizzeriaConfig config = mapper.readValue(json, PizzeriaConfig.class);

        List<Integer> bakerCount = new ArrayList<>();
        List<Integer> courierCount = new ArrayList<>();

        ShutdownStrategy strategy = (generatorThread, bakerThreads, courierThreads, orderQueue, storage) -> {
            bakerCount.add(bakerThreads.size());
            courierCount.add(courierThreads.size());
            generatorThread.interrupt();
            orderQueue.stopAccepting();
            for (Thread t : bakerThreads) t.join();
            storage.close();
            for (Thread t : courierThreads) t.join();
        };

        Pizzeria pizzeria = new Pizzeria(config, strategy);
        pizzeria.start();

        assertEquals(2, bakerCount.get(0));
        assertEquals(3, courierCount.get(0));
    }

    @Test
    void startDeliversAllOrdersGracefully() throws Exception {
        PizzeriaConfig config = buildConfig(3, 1000);
        Pizzeria pizzeria = new Pizzeria(config);
        assertDoesNotThrow(pizzeria::start);
    }
}


