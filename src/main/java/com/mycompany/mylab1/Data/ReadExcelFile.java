package com.mycompany.mylab1.Data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadExcelFile {
    private Workbook workbook;

    // Конструктор класса
    public ReadExcelFile(String pathname) throws IOException {

        File file = new File(pathname);

        if (!file.exists()) {
            throw new IllegalArgumentException("Файл не найден : " + pathname);
        }

        FileInputStream fis = new FileInputStream(file);

        this.workbook = new XSSFWorkbook(fis); // Для .xlsx файлов
    }

    // Метод для получения всех вкладок из экселя
    public List<String> getSheetNames() {

        List<String> sheetNames = new ArrayList<>();

        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
        }

    // Метод для получения всех названий колонок для вкладки
    public List<String> getSheetHeader(String sheetName) {

        List<String> header = new ArrayList<>();

        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            throw new IllegalArgumentException("Вкладки : " + sheetName + " - нет");
        }

        // Первая строка считается заголовком
        Row headerRow = sheet.getRow(0);

        if (headerRow == null) {
            throw new IllegalStateException("Первая строка  отсутствует во вкладке : " + sheetName);
        }

        for (int j = 0; j < headerRow.getLastCellNum(); j++) {
            Cell headerCell = headerRow.getCell(j);
                header.add( getCellValueAsString(headerCell) );
        }
        return header;

    }

    // Метод для получения индекса колонки по её имени
    public int getColumnIndex(String sheetName, String columnName) {

        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new IllegalArgumentException("Вкладки : " + sheetName + " - нет");
        }

        // первая строка
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalStateException("Первая строка отсутствует во вкладке : " + sheetName);
        }

        // Находим индекс колонки по её имени
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell headerCell = headerRow.getCell(i);
            if (headerCell != null && columnName.equalsIgnoreCase(getCellValueAsString(headerCell))) {
                return i;
            }
        }

        throw new IllegalArgumentException("Колонки с именем : " + columnName + " - нет");
    }


    // Метод для получения из ячейки данных String
    private String getCellValueAsString(Cell cell) {

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (evaluator.evaluateFormulaCell(cell)) {
                    case NUMERIC:
                        return String.valueOf(cell.getNumericCellValue());
                    default:
                        return cell.getCellFormula();
                }
            default:
                return "";
        }
    }

    // Метод для получения из ячейки данных Double
    private Double getCellValueAsDouble(Cell cell) {

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        switch (cell.getCellType()) {
            case NUMERIC:
                return Double.valueOf(cell.getNumericCellValue());
            case FORMULA:
                switch (evaluator.evaluateFormulaCell(cell)) {
                    case NUMERIC:
                        return Double.valueOf(cell.getNumericCellValue());
                    default:
                        return Double.valueOf(cell.getCellFormula());
                }
            default:
                return null;
        }
    }

    // Метод для получения массива данных из конкретной колонки
    // для выбранной вкладки и колони получаем выборку с данными
    public Double[] getColumnData(String sheetName, String columnName) {

        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new IllegalArgumentException("Вкладки : " + sheetName + " - нет");
        }

        Row headerRow = sheet.getRow(0); // Первая строка считается заголовком
        if (headerRow == null) {
            throw new IllegalStateException("Первая строка отсутствует во вкладке : " + sheetName);
        }

        // Находим индекс колонки по её имени
        int columnIndex = getColumnIndex(sheetName, columnName);

        // Собираем данные из колонки
        List<Double> columnData = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue; // Пропускаем пустые строки
            }
            Cell cell = row.getCell(columnIndex);
            columnData.add(getCellValueAsDouble(cell));
        }

        // Преобразуем список в массив
        return columnData.toArray(new Double[0]);
    }

    // Метод для закрытия рабочей книги
    public void closeWorkbook() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
    }


}

