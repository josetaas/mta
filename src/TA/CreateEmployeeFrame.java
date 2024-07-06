package TA;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class CreateEmployeeFrame extends EmployeeDetailsBaseFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String EMPLOYEE_DATA_FILE = "MotorPH Employee Data.csv";
    private PayrollSystem payrollSystem;

    public CreateEmployeeFrame(PayrollSystem payrollSystem) {
        super("Create Employee");
        this.payrollSystem = payrollSystem;

        actionButton.setText("Create");
        actionButton.addActionListener(this);
    }

    @Override
    protected void onActionButtonClicked() {
        String[] newEmployeeDetails = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            newEmployeeDetails[i] = "\"" + fields[i].getText().replace("\"", "\"\"") + "\"";
        }

        // Save new employee details to CSV
        try (FileWriter fw = new FileWriter(EMPLOYEE_DATA_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(String.join(",", newEmployeeDetails));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving employee data.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Update the table in PayrollSystem
        payrollSystem.addEmployeeToTable(newEmployeeDetails);

        JOptionPane.showMessageDialog(this, "New Employee Created Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        onActionButtonClicked();
    }
}
