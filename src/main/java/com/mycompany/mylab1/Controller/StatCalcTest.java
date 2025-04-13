package com.mycompany.mylab1.Controller;


import com.mycompany.mylab1.Model.ReadExcelFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class StatCalcTest
{
    public static void main( String[] args )  {

        String pathname = "src/main/resources/Лаба_1 образцы данных.xlsx";
        try {
            ReadExcelFile excelReader = new ReadExcelFile(new FileInputStream( new File(pathname))
            );
            excelReader.getSheetNames();

            String[] sheetsArray = new String[excelReader.getSheetNames().size()];

            for (int i = 0; i < excelReader.getSheetNames().size(); i++) {
                sheetsArray[i] = excelReader.getSheetNames().get(i);
                System.out.println(sheetsArray[i]);
            }

            // Получаем список вкладок
            List<String> sheetNames = excelReader.getSheetNames();
            System.out.println("Список вкладок: " + sheetNames);

            for (String sheetName : sheetNames) {
                System.out.println("Данные из вкладки : " + sheetName);
                System.out.println("Заголовок : " + excelReader.getSheetHeader(sheetName));

                List<String> headerNames = excelReader.getSheetHeader(sheetName);
                for(String header : headerNames){
                    Double[] colData = excelReader.getColumnData(sheetName, header);
                    System.out.println("Данные из колонки : " + header);
                    //for (Double value : colData) {
                    //   System.out.println(value);
                    //}
                }
            }

            // Расчет статистики

            String sheetNameOut = "Вариант 3";
            System.out.println("Данные из вкладки : " + sheetNameOut);

            StatCalc statData = new StatCalc( sheetNameOut );

            List<String> headerNames = excelReader.getSheetHeader(sheetNameOut);
            for(String header : headerNames) {
                //System.out.println("Заголовок : " + excelReader.getSheetHeader(sheetNameOut));
                Double[] colData = excelReader.getColumnData(sheetNameOut, header);
                statData.addData(colData);
                statData.calcStatistics();
                statData.calcConfidenceInterval(0.03);
                for(String h: headerNames) {
                    if(!header.equals(h)) {
                        Double[] colOther = excelReader.getColumnData(sheetNameOut, h);
                        String covName = header + "-" + h;
                        statData.calcCovariance(covName, colData, colOther);
                    }
                }
            }
            System.out.println(statData.toString() );
            statData.printSortedData();

            String pathnameOut = "src/main/resources/Лаба_1 образцы данных_результаты.xlsx";
            // save to file




            excelReader.closeWorkbook();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

    }
}

