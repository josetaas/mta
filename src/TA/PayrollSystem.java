package TA;

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
    public Boolean isSuperAdmin = false;
    public String employeeID;

    private static final long serialVersionUID = 1L;
    private JButton logoutButton;
    private JButton createButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField searchField;
    private JTable employeeTable;
    private EmployeeTableModel tableModel;
    private static final String EMPLOYEE_DATA_FILE = "MotorPH Employee Data.csv";
    private String[] employeeDetails;

    public PayrollSystem(String employeeID) {
        super("MotorPH Payroll System");
        this.employeeID = employeeID;

        // Search bar
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
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

        // Check if employee is a super admin
        employeeDetails = tableModel.getEmployeeDetailsByID(this.employeeID);
        if (employeeDetails != null) {
            // If position doesn't contain the string Rank and File, set as super admin
            isSuperAdmin = !employeeDetails[11].contains("Rank and File");
        }

        if (!isSuperAdmin) {
            createButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
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

    public void updateEmployeeInTable(String[] employeeDetails) {
        tableModel.updateEmployee(employeeDetails);
    }

    private void openEmployeeDetailsFrame(int row) {
        String[] employeeDetails = tableModel.getEmployeeDetails(row);
        new EmployeeDetailsFrame(employeeDetails, this).setVisible(true);
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
        } else if (e.getSource() == searchButton) {
          // get input from searchField
          String search = searchField.getText();
          if (search.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an employee ID to search.", "No Input", JOptionPane.WARNING_MESSAGE);
          } else {
            new EmployeeDetailsFrame(tableModel.getEmployeeDetailsByID(search), this).setVisible(true);
          }
        }
    }

    private class EmployeeTableModel extends AbstractTableModel {
        private List<String[]> employees = new ArrayList<>();
        private String[] columnNames = { "Employee ID", "Last Name", "First Name", "Position" };

        private final int[] columnIndices = { 0, 1, 2, 11 }; // Indices corresponding to Employee ID, Last Name, First Name, and Position

        public void addEmployee(String[] employeeDetails) {
            employees.add(employeeDetails);
            fireTableRowsInserted(employees.size() - 1, employees.size() - 1);
        }

        public String[] getEmployeeDetailsByID(String employeeID) {
          for (String[] employee : employees) {
              if (employee[0].replace("\"", "").equals(employeeID.replace("\"", ""))) {
                  return employee;
              }
          }
          return null;
        }

        public void updateEmployee(String[] employeeDetails) {
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(i)[0].replace("\"", "").equals(employeeDetails[0].replace("\"", ""))) { // Compare by Employee ID
                    employees.set(i, employeeDetails);
                    fireTableRowsUpdated(i, i);
                    break;
                }
            }
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
            String value = employees.get(rowIndex)[columnIndices[columnIndex]];
            return value.replace("\"", ""); // Strip quotes from the value before returning
        }
    }
}
