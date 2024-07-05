package MS1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class PayrollSystem extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton logoutButton;
    private JButton createButton;
    private JButton deleteButton;
    private JTextField searchField;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private static final String EMPLOYEE_DATA_FILE = "MotorPH Employee Data.csv";

    public PayrollSystem(String employeeID) {
        super("MotorPH Payroll System");

        // Search bar
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        // Employee table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Employee ID");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Birthday");
        employeeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        // Buttons at the bottom
        createButton = new JButton("Create");
        createButton.addActionListener(this);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);

        // Logout button
        logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(this);

        // Layout setup
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(createButton);
        bottomPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load employee data
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        try (Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(EMPLOYEE_DATA_FILE));
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                     .build()) {
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                tableModel.addRow(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                LoginScreen loginScreen = new LoginScreen();
                loginScreen.setVisible(true);
            }
        } else if (e.getSource() == createButton) {
            // Handle the creation of a new employee (this could open a new dialog/form)
            JOptionPane.showMessageDialog(this, "Create employee functionality not implemented yet.");
        } else if (e.getSource() == deleteButton) {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                // Handle the deletion from the data source as well (e.g., CSV, database)
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == searchField) {
            // Implement search functionality
            JOptionPane.showMessageDialog(this, "Search functionality not implemented yet.");
        }
    }
}
