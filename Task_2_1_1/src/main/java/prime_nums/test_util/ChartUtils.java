package prime_nums.test_util;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.util.List;

public class ChartUtils {
    public static void buildAndSaveChart(List<String> xData, List<Double> yData) throws IOException {
        CategoryChart chart = new CategoryChartBuilder()
                .width(1000)
                .height(600)
                .title("Non-Prime Detection Performance")
                .xAxisTitle("Algorithm / Threads")
                .yAxisTitle("Time (ms)")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

        chart.addSeries("Execution Time", xData, yData);

        BitmapEncoder.saveBitmap(chart, "./benchmark_chart", BitmapEncoder.BitmapFormat.PNG);
        System.out.println("Chart saved to ./benchmark_chart.png");
    }
}
