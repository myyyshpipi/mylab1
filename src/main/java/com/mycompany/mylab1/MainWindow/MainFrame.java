package com.mycompany.mylab1.MainWindow;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private MainFrame frame;
    private JTextField inputFile = new JTextField("", 30);
    private JTextField outputFile = new JTextField("", 30);

    private String allSheets[] = new String [0];


    public MainFrame() {
        this.frame = this;
        //frame = new JFrame(title);

        // основная панель
        JPanel mainPanel = new JPanel( new GridLayout(4, 1, 5,20) );

        // панель с кнопками
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4));
        JButton loadFileButton = new JButton("Загрузить");
        JButton calcDataButton = new JButton("Расчет");
        JButton saveFileButton = new JButton("Выгрузить");
        JButton exitButton = new JButton("Выйти");

        buttonsPanel.add(loadFileButton);
        buttonsPanel.add(calcDataButton);
        buttonsPanel.add(saveFileButton);
        buttonsPanel.add(exitButton);


        // панель с данными входного файла
        JPanel loadFilePanel = new JPanel( new GridLayout(2, 1, 5, 5) );
        JLabel loadFileLabel = new JLabel("Файл с входными данными");
        //loadFilePanel.setLayout();
        loadFilePanel.add(loadFileLabel);
        loadFilePanel.add(inputFile);

        // панель для выбора вкладки с выборками и расчета данных
        JPanel calcDataPanel = new JPanel( new GridLayout(2, 1, 5, 5) );
        JLabel lblSheets = new JLabel("Вкладки в файле");

        JList lstAllSheets = new JList(allSheets);
        //JList lstAllSheets = new JList();
        //lstAllSheets.add(lblSheets, BorderLayout.PAGE_START);
        lstAllSheets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstAllSheets.setSelectedIndex(0);
        //lstAllSheets.addListSelectionListener(this);
        lstAllSheets.setBackground(Color.lightGray);
        lstAllSheets.setForeground(Color.blue);
        calcDataPanel.add(lblSheets);
        calcDataPanel.add(lstAllSheets);

        // панель с данными для сохранения в файл
        JPanel saveFilePanel = new JPanel( new GridLayout(2, 1, 5, 5) );
        JLabel saveFileLabel = new JLabel("Файл с выходными данными");
        saveFilePanel.add(saveFileLabel);
        saveFilePanel.add(outputFile);


        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(loadFilePanel, BorderLayout.SOUTH);
        mainPanel.add(calcDataPanel, BorderLayout.SOUTH);
        mainPanel.add(saveFilePanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setBounds(200, 200, 480, 240);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



}
