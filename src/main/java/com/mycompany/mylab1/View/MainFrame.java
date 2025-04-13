package com.mycompany.mylab1.View;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private JFrame frame;

    private JComboBox<String> datasetSelector;

    private JTextField inputFilePath;
    private JTextField outputFilePath;
    private JTextField importResultInfo;

    private JButton loadFileButton;
    private JButton saveFileButton;
    private JButton importDataButton;
    private JButton calcStatButton;
    private JButton exitButton;

    public MainFrame() {
            initGUI();
    }

        private void initGUI() {
            this.frame = new JFrame("Калькулятор статистики из эксель файла");
            this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.frame.setBounds(200, 200, 640, 480);
            this.frame.setLayout(new BorderLayout());


            // панель с кнопками
            JPanel buttonsPanel = new JPanel(new GridLayout(1, 5));
            this.loadFileButton = new JButton("Выбрать Excel");
            this.importDataButton = new JButton("Импорт данных");
            this.calcStatButton = new JButton("Расчет");
            this.saveFileButton = new JButton("Выгрузить в файл");
            this.exitButton = new JButton("Выйти");

            buttonsPanel.add(loadFileButton);
            buttonsPanel.add(importDataButton);
            buttonsPanel.add(calcStatButton);
            buttonsPanel.add(saveFileButton);
            buttonsPanel.add(exitButton);


            // панель с данными входного файла
            JPanel loadFilePanel = new JPanel( new GridLayout(2, 1, 5, 5) );
            JLabel loadFileLabel = new JLabel("Файл с входными данными :");
            inputFilePath = new JTextField("src/main/resources/Лаба_1 образцы данных.xlsx", 30);
            //loadFilePanel.setLayout();
            loadFilePanel.add(loadFileLabel);
            loadFilePanel.add(inputFilePath);

            // панель для выбора вкладки с выборками и расчета данных
            JPanel calcDataPanel = new JPanel( new GridLayout(2, 1, 5, 5) );
            JLabel lblSheets = new JLabel("Вкладки в файле:");
            calcDataPanel.add(lblSheets);
            datasetSelector = new JComboBox<>();
            calcDataPanel.add(datasetSelector);

            // панель результата импорта данных
            JPanel importDataPanel = new JPanel( new GridLayout(2, 1, 5, 5) );
            JLabel importDataLabel = new JLabel("Импорт данных :");
            importResultInfo = new JTextField("none", 30);
            importDataPanel.add(importDataLabel);
            importDataPanel.add(importResultInfo);


            // панель с данными для сохранения в файл
            JPanel saveFilePanel = new JPanel( new GridLayout(2, 1, 5, 5) );
            JLabel saveFileLabel = new JLabel("Файл с выходными данными :");
            outputFilePath = new JTextField("src/main/resources/Лаба_1 образцы данных_расчет.xlsx", 30);
            saveFilePanel.add(saveFileLabel);
            saveFilePanel.add(outputFilePath);

            // основная панель
            JPanel mainPanel = new JPanel( new GridLayout(5, 1, 5,20) );

            mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
            mainPanel.add(loadFilePanel, BorderLayout.SOUTH);
            mainPanel.add(calcDataPanel, BorderLayout.SOUTH);
            mainPanel.add(importDataPanel, BorderLayout.SOUTH);
            mainPanel.add(saveFilePanel, BorderLayout.SOUTH);

            frame.getContentPane().add(mainPanel);
            frame.pack();
            frame.setVisible(true);

        }

    public JComboBox<String> getDatasetSelector() {
        return datasetSelector;
    }

    public JFrame getFrame() {
        return frame;
    }

    public JButton getLoadButton() {
        return loadFileButton;
    }
    public JButton getImportDataButton() {
        return importDataButton;
    }
    public JButton getSaveButton() {
        return saveFileButton;
    }
    public JButton getExitButton() {
        return exitButton;
    }

    public JButton getCalcStatButton() {
        return calcStatButton;
    }

    public void showResult(String result) {
        importResultInfo.setText(result);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JTextField getInputFilePath() {
        return inputFilePath;
    }

    public JTextField getOutputFilePath() {
        return outputFilePath;
    }

}
