package com.mycompany.mylab1.Model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.*;

public class DataStorage {
    private String dataName;    // название вкладки
    private Map<String, List<Double>> inputDatasets;  // колонки с данными
    private Map<String, HashMap<String, Double>> outputResults; // результаты для каждой колонки

    public DataStorage(){
        this.inputDatasets = new HashMap<>();
        this.outputResults = new HashMap<>();
    }

    public void addResult(String keyDataset, String keyResultData, double valueResultData) {
        // Получаем внутреннюю карту по внешнему ключу, если она существует,
        // или создаем новую, если ее нет
        HashMap<String, Double> innerMap = outputResults.getOrDefault(keyDataset, new HashMap<>());

        // Добавляем или обновляем значение в внутренней карте
        innerMap.put(keyResultData, valueResultData);

        // Обновляем внешнюю карту
        outputResults.put(keyDataset, innerMap);
    }
    public double getResultValue(String keyDataset, String keyResultData) {
        return outputResults.getOrDefault(keyDataset, new HashMap<>()).get(keyResultData);
    }

    public Map<String, HashMap<String, Double>> getOutputResults() {
        return outputResults;
    }

    public List<String> getDatasetNames() {
        return new ArrayList<>(inputDatasets.keySet());
    }

    public List<String> getResultsNames() {
        return new ArrayList<>(outputResults.keySet());
    }
    public List<String> getResultsKeys(String dataset) {
        return new ArrayList<>(outputResults.get(dataset).keySet());
    }


    public HashMap<String, Double> getDatasetResults(String datasetName) {
        HashMap<String, Double> datasetResults = outputResults.get(datasetName);
        if (datasetResults == null || datasetResults.isEmpty()) {
            throw new IllegalStateException("No data for datasetRESULTS: " + datasetName);
        }
        return datasetResults;
    }


    public void addDataset(String keyDataSet, List<Double> valueDataSet) {
        inputDatasets.put(keyDataSet, valueDataSet);
    }

    public List<Double> getDataset(String keyDataSet) {
        return inputDatasets.get(keyDataSet);
    }
    public Double[] getDatasetArray(String keyDataSet) {
        return inputDatasets.get(keyDataSet).toArray(new Double[0]);
    }




    public HashMap<String, Double> getResult(String keyDataset) {
        // Получаем внутреннюю карту по внешнему ключу
        HashMap<String, Double> innerMap = outputResults.get(keyDataset);

        // Если внутренняя карта существует, получаем значение по внутреннему ключу
        if (innerMap != null) {
            return innerMap;
        }

        // Возвращаем null, если внутренней карты или значения нет
        return null;
    }

    public Map<String, List<Double>> getAllDatasets() {
        return inputDatasets;
    }

    public Map<String, HashMap<String, Double>> getAllResults() {
        return outputResults;
    }


    public void printResultDataSorted() {
        Map<String, Map<String, Double>> sortedStorage = new TreeMap<>(outputResults);

        for (Map.Entry<String, Map<String, Double>> entry : sortedStorage.entrySet()) {
            System.out.println("Выборка: " + entry.getKey());
            // Создаем TreeMap для сортировки ключей второго уровня
            Map<String, Double> sortedSubData = new TreeMap<>(entry.getValue());
            for (Map.Entry<String, Double> subEntry : sortedSubData.entrySet()) {
                System.out.println("  Параметр: " + subEntry.getKey() + ", Значение: " + subEntry.getValue());
            }
        }
    }

    @Override
    public String toString() {
        return "DataStorage{" +
                "outputResults=" + outputResults +
                '}';
    }
}