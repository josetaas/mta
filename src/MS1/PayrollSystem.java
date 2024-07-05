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
import java.util.Arrays;

public class PayrollSystem extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton logoutButton;
    private JTextArea employeeDetailsTextArea;
    private JTextArea payslipTextArea;
    private JButton searchButton;
    private JButton viewEmployeeButton;
    private JTextField searchField;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private static final String EMPLOYEE_DATA_FILE = "/MotorPH Employee Data.csv";

    public PayrollSystem(String employeeID) {
        super("MotorPH Payroll System");

        JLabel employeeDetailsLabel = new JLabel("Employee Details:");
        employeeDetailsTextArea = new JTextArea(10, 40);
        employeeDetailsTextArea.setEditable(false);
        payslipTextArea = new JTextArea(10, 40);
        payslipTextArea.setEditable(false);
        JScrollPane employeeScrollPane = new JScrollPane(employeeDetailsTextArea);
        JScrollPane payslipScrollPane = new JScrollPane(payslipTextArea);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        viewEmployeeButton = new JButton("View Employee");
        viewEmployeeButton.addActionListener(this);
        logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(this);

        searchField.setPreferredSize(new Dimension(200, searchField.getPreferredSize().height));
        employeeScrollPane.setPreferredSize(new Dimension(400, 150));
        payslipScrollPane.setPreferredSize(new Dimension(400, 150));

        String[] columnNames = {"Employee Number", "Last Name", "First Name", "Position"};
        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        loadEmployeeData();

        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 200));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Enter Employee ID:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(searchField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(searchButton, gbc);

        gbc.gridx = 3;
        panel.add(logoutButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(tableScrollPane, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        panel.add(viewEmployeeButton, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 4;
        panel.add(employeeDetailsLabel, gbc);

        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        panel.add(employeeScrollPane, gbc);

        gbc.gridy = 5;
        panel.add(new JLabel("Payslip View:"), gbc);

        gbc.gridy = 6;
        panel.add(payslipScrollPane, gbc);

        add(panel);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    private void loadEmployeeData() {
    	  try (Reader reader = new InputStreamReader(PayrollSystem.class.getResourceAsStream(EMPLOYEE_DATA_FILE));
    	       CSVReader csvReader = new CSVReaderBuilder(reader)
    	           .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
    	           .build()) {
    	    // Skip the header row
    	    csvReader.readNext();

    	    String[] nextLine;
    	    while ((nextLine = csvReader.readNext()) != null) {
    	      // Extract only the necessary columns (index may vary based on your data structure)
    	      String[] rowData = {
    	          nextLine[0], // Employee Number
    	          nextLine[1], // Last Name
    	          nextLine[2], // First Name
    	          nextLine[11], // Position (adjust the index based on your data)
    	      };
    	      tableModel.addRow(rowData);
    	    }
    	  } catch (IOException | CsvValidationException e) {
    	    e.printStackTrace();
    	  }
    	}
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String employeeID = searchField.getText();
            String employeeDetailsText = null;
            String payslipText = null;

            try (FileReader reader = new FileReader(EMPLOYEE_DATA_FILE);
                 CSVReader csvReader = new CSVReaderBuilder(reader)
                         .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                         .build()) {
                String[] nextLine;
                while ((nextLine = csvReader.readNext()) != null) {
                    if (nextLine[0].equals(employeeID)) {
                        employeeDetailsText = String.join("\n", Arrays.copyOfRange(nextLine, 1, 5));
                        payslipText = String.join("\n", Arrays.copyOfRange(nextLine, 5, nextLine.length));
                        break;
                    }
                }
            } catch (IOException | CsvValidationException e1) {
                e1.printStackTrace();
            }

            employeeDetailsTextArea.setText(employeeDetailsText != null ? employeeDetailsText : "Employee not found");
            payslipTextArea.setText(payslipText != null ? payslipText : "Payslip not found");
        } else if (e.getSource() == logoutButton) {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                LoginScreen loginScreen = new LoginScreen();
                loginScreen.setVisible(true);
            }
        } else if (e.getSource() == viewEmployeeButton) {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                String employeeID = (String) tableModel.getValueAt(selectedRow, 0);
                new EmployeeDetailsFrame(employeeID).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an employee from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
