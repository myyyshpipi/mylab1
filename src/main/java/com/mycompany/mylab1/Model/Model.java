package com.mycompany.mylab1.Model;

import java.util.List;

public class Model {
    private DataStorage dataStorage;

    public Model() {
        this.dataStorage = new DataStorage();
    }

    public void addDataset(String name, List<Double> data) {
        dataStorage.addDataset(name, data);
    }

    public List<Double> getDataset(String name) {
        return dataStorage.getDataset(name);
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }
}