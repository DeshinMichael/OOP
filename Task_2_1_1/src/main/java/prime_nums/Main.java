package prime_nums;

import prime_nums.detector.SequentialDetector;
import prime_nums.detector.StreamDetector;
import prime_nums.detector.ThreadDetector;
import prime_nums.test_util.BenchmarkUtils;
import prime_nums.test_util.ChartUtils;
import prime_nums.test_util.DataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        int size = 1_000_000;

        System.out.println("Generating data...");
        int[] numbers = DataProvider.generateAllPrimes(size, 1000003);

        System.out.println("Start benchmarking with array size: " + size);

        List<String> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();

        double seqTime = BenchmarkUtils.measure(new SequentialDetector(), numbers, "Sequential");
        xData.add("Sequential");
        yData.add(seqTime);

        for (int i = 2; i <= 10; i++) {
            String label = "Threads (" + i + ")";
            double threadTime = BenchmarkUtils.measure(new ThreadDetector(i), numbers, label);
            xData.add(label);
            yData.add(threadTime);
        }

        int manyThreads = 20;
        String labelMany = "Threads (" + manyThreads + ")";
        double threadManyTime = BenchmarkUtils.measure(new ThreadDetector(manyThreads), numbers, labelMany);
        xData.add(labelMany);
        yData.add(threadManyTime);

        double streamTime = BenchmarkUtils.measure(new StreamDetector(), numbers, "Stream");
        xData.add("Stream");
        yData.add(streamTime);

        ChartUtils.buildAndSaveChart(xData, yData);
    }
}
