package MS1;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class PayrollSystem extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton logoutButton;
    private JButton createButton;
    private JButton deleteButton;
    private JTextField searchField;
    private JTable employeeTable;
    private EmployeeTableModel tableModel;
    private static final String EMPLOYEE_DATA_FILE = "MotorPH Employee Data.csv";
    private String employeeID;

    public PayrollSystem(String employeeID) {
        super("MotorPH Payroll System");
        this.employeeID = employeeID;

        // Search bar
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);

        // Employee table
        tableModel = new EmployeeTableModel();
        employeeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);

        // Add mouse listener for double-click
        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = employeeTable.getSelectedRow();
                    if (row != -1) {
                        openEmployeeDetailsFrame(row);
                    }
                }
            }
        });

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
        try (Reader reader = new FileReader(EMPLOYEE_DATA_FILE);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                     .build()) {
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                tableModel.addEmployee(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private List<String[]> readAllEmployees() {
        List<String[]> employees = new ArrayList<>();
        try (Reader reader = new FileReader(EMPLOYEE_DATA_FILE);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                     .build()) {
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                employees.add(nextLine);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private void writeAllEmployees(List<String[]> employees) {
        try (Writer writer = new FileWriter(EMPLOYEE_DATA_FILE);
             ICSVWriter csvWriter = new CSVWriterBuilder(writer).withSeparator(',').build()) {
            for (String[] employee : employees) {
                csvWriter.writeNext(employee);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEmployeeToTable(String[] employeeDetails) {
        tableModel.addEmployee(employeeDetails);
    }

    private void openEmployeeDetailsFrame(int row) {
        String[] employeeDetails = tableModel.getEmployeeDetails(row);
        new EmployeeDetailsFrame(employeeDetails).setVisible(true);
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
            new CreateEmployeeFrame(this).setVisible(true);
        } else if (e.getSource() == deleteButton) {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeEmployee(selectedRow);

                // Read all employees from the CSV
                List<String[]> employees = readAllEmployees();

                // Remove the selected employee
                employees.remove(selectedRow);

                // Write the updated list back to the CSV
                writeAllEmployees(employees);

                JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == searchField) {
            // Implement search functionality
            JOptionPane.showMessageDialog(this, "Search functionality not implemented yet.");
        }
    }

    private class EmployeeTableModel extends AbstractTableModel {
        private List<String[]> employees = new ArrayList<>();
        private String[] columnNames = { "Employee ID", "Last Name", "First Name", "Position" };

        public void addEmployee(String[] employeeDetails) {
            employees.add(employeeDetails);
            fireTableRowsInserted(employees.size() - 1, employees.size() - 1);
        }

        public void removeEmployee(int row) {
            employees.remove(row);
            fireTableRowsDeleted(row, row);
        }

        public String[] getEmployeeDetails(int row) {
            return employees.get(row);
        }

        @Override
        public int getRowCount() {
            return employees.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return employees.get(rowIndex)[columnIndex];
        }
    }
}
