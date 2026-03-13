package pizzeria.config;

import java.util.List;

public class PizzeriaConfig {
    private int storageCapacity;
    private int workingTimeMs;
    private int orderIntervalMs;
    private int totalOrders;
    private List<BakerConfig> bakers;
    private List<CourierConfig> couriers;

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public int getWorkingTimeMs() {
        return workingTimeMs;
    }

    public int getOrderIntervalMs() {
        return orderIntervalMs;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public List<BakerConfig> getBakers() {
        return bakers;
    }

    public List<CourierConfig> getCouriers() {
        return couriers;
    }
}
