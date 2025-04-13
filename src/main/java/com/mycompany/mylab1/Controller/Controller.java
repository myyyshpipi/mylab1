package com.mycompany.mylab1.Controller;

import com.mycompany.mylab1.Model.*;
import com.mycompany.mylab1.View.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Controller {
    private Model model;
    private MainFrame view;
    private JFileChooser fileChooser;
    private ReadExcelFile readExcelFile;

    public Controller(Model model, MainFrame view) {
        this.model = model;
        this.view = view;
        this.fileChooser = new JFileChooser();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        view.getLoadButton().addActionListener(e -> loadExcelFile());
        view.getDatasetSelector().addActionListener(e -> updateResultDisplay());
        view.getImportDataButton().addActionListener(e -> importDatasets());
        view.getCalcStatButton().addActionListener(e -> calcStatData());
        view.getSaveButton().addActionListener(e -> saveExcelFile());
        view.getExitButton().addActionListener(e -> exitProgram());
    }

    private void exitProgram() {
        System.exit(0);
        //view.getFrame().setVisible(false);
        //view.getFrame().dispose();
    }

    private void loadExcelFile() {
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            view.getInputFilePath().setText(file.getAbsolutePath());

            try (FileInputStream fis = new FileInputStream(file)){
                readExcelFile = new ReadExcelFile(fis);

                model.getDataStorage().getAllDatasets().clear();
                view.getDatasetSelector().removeAllItems();
                for (String sheet: readExcelFile.getSheetNames()){
                    view.getDatasetSelector().addItem(sheet);
                }

                if (view.getDatasetSelector().getItemCount() > 0) {
                    view.getDatasetSelector().setSelectedIndex(0);
                    updateResultDisplay();
                }
            } catch (Exception ex) {
                view.showError("Error reading Excel: " + ex.getMessage());
            }
        }
    }

    private void importDatasets() {
        model.getDataStorage().getAllDatasets().clear();
        String currentSheet = view.getDatasetSelector().getSelectedItem().toString();
        if( currentSheet != null ) {
            List<String> headerNames = readExcelFile.getSheetHeader(currentSheet);;
            for(String header : headerNames){
                List<Double> columnData = readExcelFile.getColumnListData(currentSheet, header);
                //System.out.println("Данные из колонки : " + header);
                model.addDataset(header, columnData);
                //System.out.println( model.getDataset(header) );
            }
        }
    }

    private void updateResultDisplay() {
        String selectedSheet = (String) view.getDatasetSelector().getSelectedItem();
        if (selectedSheet != null) {
            try {
                //double average = model.calculateAverage(selectedDataset);
                view.showResult(selectedSheet);
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
    }

    private void calcStatData() {

        String currentSheet = view.getDatasetSelector().getSelectedItem().toString();
        List<String> headerNames = readExcelFile.getSheetHeader(currentSheet);

        for(String header : headerNames) {

            SummaryStatistics stats = new SummaryStatistics();

            for (double value : model.getDataset(header)) {
                stats.addValue(value);
            }

            // коэффициенты ковариации для текущий выборки по отношению к другим
            Double[] colData = readExcelFile.getColumnData(currentSheet, header);
            for(String headerOther: headerNames) {
                if(!header.equals(headerOther)) {
                    Double[] colOther = readExcelFile.getColumnData(currentSheet, headerOther);
                    String covKey = "коэффициент ковариации " + header + "-" + headerOther;
                    double covValue = calcCovariance(colData, colOther);
                    model.getDataStorage().addResult( header, covKey, covValue);
                }
            }

            // доверительный интервал для мат. ожидания
            double confidenceValue = calcConfidenceInterval(stats, 0.03);
            model.getDataStorage().addResult( header, "доверительный интервал для мат ожидания", confidenceValue);

            // среднее геометрическое
            model.getDataStorage().addResult( header, "среднее геометрическое", stats.getGeometricMean());
            // среднее арифметическое
            model.getDataStorage().addResult( header, "среднее арифметическое", stats.getMean());
            // стандартное отклонение
            model.getDataStorage().addResult( header, "стандартное отклонение", stats.getStandardDeviation());
            // размах
            model.getDataStorage().addResult( header, "размах", stats.getMax() - stats.getMin());
            // количество элементов
            model.getDataStorage().addResult( header, "количество элементов", (double) stats.getN());
            // оценка дисперсии
            model.getDataStorage().addResult( header, "оценка дисперсии", stats.getPopulationVariance());
            // коэффициент вариации
            model.getDataStorage().addResult( header, "коэффициент вариации", stats.getStandardDeviation()/stats.getMean());
            // максимум
            model.getDataStorage().addResult( header, "максимум", stats.getMax());
            // минимум
            model.getDataStorage().addResult( header, "минимум", stats.getMin());
        }

        System.out.println(model.getDataStorage().getAllResults());
        model.getDataStorage().printResultDataSorted();


    }

    // расчет ковариации для двух массивов
    public double calcCovariance(Double[] data, Double[] other){
        double[] values1 = ArrayUtils.toPrimitive(data);
        double[] values2 = ArrayUtils.toPrimitive(other);
        if (values1.length != values2.length) {
            throw new IllegalArgumentException("Количество элементов в выборках разное");
        }
        Covariance cov = new Covariance();
        double covValue = cov.covariance(values1, values2);
        return covValue;
    }

    // расчет доверительного интервала для мат. ожидания
    public double calcConfidenceInterval(SummaryStatistics stats, double confidenceLevel){
        if (confidenceLevel <= 0 || confidenceLevel >= 1) {
            throw new IllegalArgumentException("Значение доверительного уровня должен быть между 0 и 1.");
        }
        TDistribution tDist = new TDistribution(stats.getN()-1);
        double a = tDist.inverseCumulativeProbability(1-confidenceLevel/2); // Inverse cumulative probability or quantile functions
        double confidenceValue = a * stats.getStandardDeviation()/Math.sqrt(stats.getN());
        return confidenceValue;

    }


    private void saveExcelFile() {

        DataStorage storage = model.getDataStorage();

        Map<String, Map<String, Double>> sortedResults = new TreeMap<>(storage.getOutputResults());



        if (storage.getAllResults().isEmpty()) {
            view.showError("No results to save");
            return;
        }

        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            view.getOutputFilePath().setText(file.getAbsolutePath());

            try (FileOutputStream fos = new FileOutputStream(file);
                Workbook workbook = new XSSFWorkbook()) {
                String sheetResults = view.getDatasetSelector().getSelectedItem().toString();
                Sheet sheet = workbook.createSheet( sheetResults + " - расчеты");



                // Заголовок таблицы
                int rowNum = 0;
                int colNum = 0;
                int colNum1 = 0;
                int colNum2 = 0;
                Row headerRow = sheet.createRow(rowNum);
                for(String dataset : sortedResults.keySet()) {
                    colNum1 = colNum*2 + 0;
                    colNum2 = colNum*2 + 1;
                    headerRow.createCell(colNum1).setCellValue("Key -" + dataset);
                    headerRow.createCell(colNum2).setCellValue("Value - " + dataset);
                    colNum++;
                }



                colNum = 0;
                colNum1 = 0;
                colNum2 = 0;
                for (Map.Entry<String, Map<String, Double>> dataset : sortedResults.entrySet()) {
                    System.out.println("Выборка: " + dataset.getKey());
                    // Создаем TreeMap для сортировки ключей второго уровня
                    Map<String, Double> sortedData = new TreeMap<>(dataset.getValue());
                    rowNum = 1;
                    colNum1 = colNum*2 + 0;
                    colNum2 = colNum*2 + 1;
                    colNum++;
                    sheet.autoSizeColumn(colNum1);
                    sheet.autoSizeColumn(colNum2);

                    Row row = sheet.createRow(rowNum);
                    Cell cell1 = row.createCell(colNum1);
                    Cell cell2 = row.createCell(colNum2);

                    for (Map.Entry<String, Double> entry : sortedData.entrySet()) {
                        System.out.println("  Параметр: " + entry.getKey() + ", Значение: " + entry.getValue());
                        row.setRowNum(rowNum++);
                        cell1.setCellValue(entry.getKey());
                        cell2.setCellValue(entry.getValue());


                        //row1.getCell(colNum1).setCellValue(entry.getKey());
                        //row1.getCell(colNum2).setCellValue(entry.getValue());
                    }

                }

                /*
                colNum = 0;
                for (Map.Entry<String, HashMap<String, Double>> entry : storage.getOutputResults().entrySet()) {
                    String dataset = entry.getKey();
                    rowNum = 1;
                    for (Map.Entry<String, Double> result : entry.getValue().entrySet()) {

                        colNum1 = colNum * 2 + 0;
                        colNum2 = colNum * 2 + 1;
                        Row row = sheet.createRow(rowNum++);
                        //row.createCell(0).setCellValue(dataset);
                        row.createCell(colNum1).setCellValue(result.getKey());
                        row.createCell(colNum2).setCellValue(result.getValue());
                    }
                }
                */



                /*
                int colNum = 0;
                for(String dataset : storage.getResultsNames()) {

                    System.out.println("ResultsName : " + dataset);

                    int rowNum = 0;
                    int colNum1 = colNum*2 + 0;
                    int colNum2 = colNum*2 + 1;
                    sheet.autoSizeColumn(colNum1);
                    sheet.autoSizeColumn(colNum2);
                    Row row = sheet.createRow(rowNum);


                    for(String datakey : storage.getResultsKeys(dataset)){
                        row.setRowNum(rowNum);

                        rowNum++;
                        System.out.println("rowNum : " + rowNum +" colNum1 : " + colNum1 +" colNum2 : " + colNum2);
                        System.out.println("ResultsKey : " + datakey + " ResultsView : " + storage.getResultValue(dataset,datakey));

                        Cell cell1 = row.createCell(colNum1);
                        Cell cell2 = row.createCell(colNum2);
                        cell1.setCellValue(datakey);
                        cell2.setCellValue(storage.getResultValue(dataset,datakey));
                        //row.createCell(colNum2).setCellValue(storage.getResultValue(dataset,datakey));

                    }
                    colNum++;
                }
                */

                //rowNum = 1;
                //for (Map.Entry<String, Double> entry : storage.getAllResults().entrySet()) {
                //    Row row = sheet.createRow(rowNum++);
                //    row.createCell(0).setCellValue(entry.getKey());
                //    row.createCell(1).setCellValue(entry.getValue());
                //}

                workbook.write(fos);
                view.showResult("Results saved to " + file.getName());

            } catch (IOException e) {
                view.showError("Error saving Excel: " + e.getMessage());
            }
        }
    }


}