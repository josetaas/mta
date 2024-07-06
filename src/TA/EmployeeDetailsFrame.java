package TA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class EmployeeDetailsFrame extends EmployeeDetailsBaseFrame {
    private static final long serialVersionUID = 1L;
    private String[] employeeDetails;
    private static final String EMPLOYEE_DATA_FILE = "MotorPH Employee Data.csv";
    private JButton saveButton;
    private PayrollSystem payrollSystem;

    public EmployeeDetailsFrame(String[] employeeDetails, PayrollSystem payrollSystem) {
        super("Employee Details");
        this.employeeDetails = employeeDetails;
        this.payrollSystem = payrollSystem;

        for (int i = 0; i < employeeDetails.length; i++) {
            fields[i].setText(employeeDetails[i].replace("\"", ""));
            fields[i].setEditable(true);
        }

        actionButton.setText("Compute Salary");
        actionButton.addActionListener(e -> onActionButtonClicked());

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> onSaveButtonClicked());

        // Retrieve the panel from the scroll pane and add the save button
        JScrollPane scrollPane = (JScrollPane) getContentPane().getComponent(0);
        JPanel panel = (JPanel) scrollPane.getViewport().getView();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 1; // Change to column 1
        gbc.gridy = 20; // Ensure it's in the 19th row
        gbc.gridwidth = 1; // Change to occupy one cell
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Change anchor to southeast
        panel.add(saveButton, gbc);

        Boolean isOwnDetails = employeeDetails[0].replace("\"", "").equals(payrollSystem.employeeID.replace("\"", ""));
        if (!payrollSystem.isSuperAdmin && !isOwnDetails) {
            saveButton.setEnabled(false);
            actionButton.setEnabled(false);
        }
    }

    @Override
    protected void onActionButtonClicked() {
        new SalaryComputationFrame(employeeDetails).setVisible(true);
    }

    private void onSaveButtonClicked() {
        String[] newEmployeeDetails = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            newEmployeeDetails[i] = "\"" + fields[i].getText().replace("\"", "\"\"") + "\"";
        }

        File tempFile = new File("temp_" + EMPLOYEE_DATA_FILE);
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DATA_FILE));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] currentDetails = line.split(",");
                // Strip quotes from employee IDs for comparison
                String currentEmployeeID = currentDetails[0].replace("\"", "");
                String newEmployeeID = newEmployeeDetails[0].replace("\"", "");

                if (currentEmployeeID.equals(newEmployeeID)) { // Compare by Employee ID
                    pw.println(String.join(",", newEmployeeDetails));
                    updated = true;
                } else {
                    pw.println(line);
                }
            }

            // If the employee ID was not found, append the new details
            if (!updated) {
                pw.println(String.join(",", newEmployeeDetails));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving employee data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Replace the old file with the updated file
        if (!tempFile.renameTo(new File(EMPLOYEE_DATA_FILE))) {
            JOptionPane.showMessageDialog(this, "Error updating employee data file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update the table in PayrollSystem
        payrollSystem.updateEmployeeInTable(newEmployeeDetails);

        JOptionPane.showMessageDialog(this, "Employee details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
