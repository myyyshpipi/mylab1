package com.mycompany.mylab1.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.HashMap;
import java.util.Map;

public class StatCalc {
    private String name;
    private SummaryStatistics stats;
    private HashMap<String, Double> results;

    // конструктор
    public StatCalc(String name) {
        this.name = name;
        this.results = new HashMap<>();
        this.stats = new SummaryStatistics();
    }

    // конструктор
    public StatCalc(String name, Double[] data ) {
        this.name = name;
        this.results = new HashMap<>();
        this.stats = new SummaryStatistics();
        addData(data );
    }

    // добавляем данные для расчета статистики
    public void addData(Double[] data ) {
        for (double value : ArrayUtils.toPrimitive(data)) {
            stats.addValue(value);
        }
    }

    // расчет статистических величит по загруженным данным
    public void calcStatistics() {
        this.results.put("среднее геометрическое", stats.getGeometricMean());     // среднее геометрическое
        this.results.put("среднее арифметическое", stats.getMean());                   // среднее арифметическое
        this.results.put("стандартное отклонение", stats.getStandardDeviation()); // стандартное отклонение
        this.results.put("размах", stats.getMax() - stats.getMin());  // размах
        this.results.put("количество элементов", (double) stats.getN());                     // количество элементов
        this.results.put("оценка дисперсии", stats.getPopulationVariance());           // дисперсия
        this.results.put("коэффициент вариации", stats.getStandardDeviation()/stats.getMean());
        this.results.put("максимум", stats.getMax());                     // максимум
        this.results.put("минимум", stats.getMin());                     // минимум
    }

    // метод расчета коварианта для двух массивов
    // необхдимо указать название коварианта
    public void calcCovariance(String covName, Double[] data, Double[] other){

        double[] values1 = ArrayUtils.toPrimitive(data);
        double[] values2 = ArrayUtils.toPrimitive(other);

        if (values1.length != values2.length) {
            throw new IllegalArgumentException("Количество элементов в выборках разное");
        }

        Covariance cov = new Covariance();
        double covValue = cov.covariance(values1, values2);
        String covKey = "коэффициент ковариации " + covName;
        this.results.put(covKey, covValue);
    }

    // расчет доверительного интервала
    public void calcConfidenceInterval(double confidenceLevel){
        if (confidenceLevel <= 0 || confidenceLevel >= 1) {
            throw new IllegalArgumentException("Значение доверительного уровня должен быть между 0 и 1.");
        }
        TDistribution tDist = new TDistribution(stats.getN()-1);
        double a = tDist.inverseCumulativeProbability(1-confidenceLevel/2);
        double confidenceValue = a * stats.getStandardDeviation()/Math.sqrt(stats.getN());
        String confidenceKey = "доверительный интервал (альфа " + confidenceLevel + ")";
        this.results.put(confidenceKey, confidenceValue);
    }

    @Override
    public String toString() {
        return "StatCalc{" +
                "results=" + results +
                '}';
    }

    public void printSortedData() {
        System.out.println("Содержимое выборки c сортировкой по ключу: ");
        results.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByKey())
                .forEach(System.out::println);
    }
}
