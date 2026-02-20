package prime_nums.test_util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChartUtilsTest {

    @AfterEach
    void cleanup() {
        File file = new File("./benchmark_chart.png");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void buildAndSaveChartShouldCreateFile() throws IOException {
        List<String> xData = List.of("Algo 1", "Algo 2");
        List<Double> yData = List.of(10.5, 20.3);

        ChartUtils.buildAndSaveChart(xData, yData);

        File file = new File("./benchmark_chart.png");
        assertTrue(file.exists(), "Chart file should be created");
    }
}
