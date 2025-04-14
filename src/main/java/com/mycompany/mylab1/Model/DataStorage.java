package com.mycompany.mylab1.Model;

import java.util.*;

public class DataStorage {
    private Map<String, List<Double>> inputDatasets;  // колонки с данными
    private Map<String, HashMap<String, Double>> outputResults; // результаты для каждой колонки

    public DataStorage() {
        this.inputDatasets = new HashMap<>();
        this.outputResults = new HashMap<>();
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

    public Map<String, List<Double>> getAllDatasets() {
        return inputDatasets;
    }


    public void addResult(String keyDataset, String keyResultData, double valueResultData) {
        // Получаем вложенный hashmap, если он существует, или создаем новый, если ее нет
        HashMap<String, Double> innerMap = outputResults.getOrDefault(keyDataset, new HashMap<>());
        // Добавляем или обновляем значение во вложенном hashmap
        innerMap.put(keyResultData, valueResultData);
        // Обновляем данные
        outputResults.put(keyDataset, innerMap);
    }

    public double getResult(String keyDataset, String keyResultData) {
        return outputResults.getOrDefault(keyDataset, new HashMap<>()).get(keyResultData);
    }

    public List<String> getDatasetNames() {
        return new ArrayList<>(inputDatasets.keySet());
    }

    public List<String> getResultsNames() {
        return new ArrayList<>(outputResults.keySet());
    }

    public List<String> getResultsKeys(String keyDataset) {
        return new ArrayList<>(outputResults.get(keyDataset).keySet());
    }
    public List<Double> getResultsValues(String keyDataset) {
        return new ArrayList<>(outputResults.get(keyDataset).values());
    }

    public HashMap<String, Double> getResultsEntry(String keyDataset) {
        HashMap<String, Double> datasetResults = outputResults.get(keyDataset);
        if (datasetResults == null || datasetResults.isEmpty()) {
            throw new IllegalStateException("No data for datasetRESULTS: " + keyDataset);
        }
        return datasetResults;
    }

    public int getResultsEntrySize(String keyDataset) {
        return outputResults.get(keyDataset).size();
    }

    public int getResultsSize() {
        return outputResults.size();
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