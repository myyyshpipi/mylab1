package com.mycompany.mylab1.Model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private DataStorage dataStorage;


    public Model(){
        this.dataStorage = new DataStorage();
    }

    public void addDataset(String name, List<Double> data) {
        dataStorage.addDataset(name, data);
    }
    public List<Double> getDataset(String name){
        return dataStorage.getDataset(name);
    }


    public DataStorage getDataStorage() {
        return dataStorage;
    }
}