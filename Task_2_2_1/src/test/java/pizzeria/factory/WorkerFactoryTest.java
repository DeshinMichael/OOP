package pizzeria.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pizzeria.config.BakerConfig;
import pizzeria.config.CourierConfig;
import pizzeria.core.OrderQueue;
import pizzeria.core.Storage;
import pizzeria.logging.PizzeriaLogger;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkerFactoryTest {

    private final PizzeriaLogger noopLogger = msg -> {};
    private final OrderQueue queue = new OrderQueue();
    private final Storage storage = new Storage(5);
    private final WorkerFactory factory = new WorkerFactory(queue, storage, noopLogger);
    private final ObjectMapper mapper = new ObjectMapper();

    private BakerConfig bakerConfig(int id, int cookingTimeMs) throws Exception {
        return mapper.readValue(
                "{\"id\":" + id + ",\"cookingTimeMs\":" + cookingTimeMs + "}",
                BakerConfig.class
        );
    }

    private CourierConfig courierConfig(int id, int trunkSize, int deliveryTimeMs) throws Exception {
        return mapper.readValue(
                "{\"id\":" + id + ",\"trunkSize\":" + trunkSize + ",\"deliveryTimeMs\":" + deliveryTimeMs + "}",
                CourierConfig.class
        );
    }

    @Test
    void createBakersReturnsCorrectCount() throws Exception {
        List<BakerConfig> configs = List.of(
                bakerConfig(1, 1000),
                bakerConfig(2, 2000)
        );
        List<Thread> threads = factory.createBakers(configs);
        assertEquals(2, threads.size());
    }

    @Test
    void createBakersThreadsHaveCorrectNames() throws Exception {
        List<BakerConfig> configs = List.of(
                bakerConfig(1, 1000),
                bakerConfig(3, 1500)
        );
        List<Thread> threads = factory.createBakers(configs);
        assertEquals("Baker-1", threads.get(0).getName());
        assertEquals("Baker-3", threads.get(1).getName());
    }

    @Test
    void createCouriersReturnsCorrectCount() throws Exception {
        List<CourierConfig> configs = List.of(
                courierConfig(1, 3, 500),
                courierConfig(2, 5, 700)
        );
        List<Thread> threads = factory.createCouriers(configs);
        assertEquals(2, threads.size());
    }

    @Test
    void createCouriersThreadsHaveCorrectNames() throws Exception {
        List<CourierConfig> configs = List.of(
                courierConfig(2, 3, 500),
                courierConfig(4, 5, 700)
        );
        List<Thread> threads = factory.createCouriers(configs);
        assertEquals("Courier-2", threads.get(0).getName());
        assertEquals("Courier-4", threads.get(1).getName());
    }

    @Test
    void createBakersEmptyListReturnsEmptyThreadList() {
        List<Thread> threads = factory.createBakers(List.of());
        assertTrue(threads.isEmpty());
    }

    @Test
    void createCouriersEmptyListReturnsEmptyThreadList() {
        List<Thread> threads = factory.createCouriers(List.of());
        assertTrue(threads.isEmpty());
    }
}
