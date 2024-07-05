package MS1;

import java.awt.event.ActionEvent;

public class EmployeeDetailsFrame extends EmployeeDetailsBaseFrame {
    private static final long serialVersionUID = 1L;
    private String[] employeeDetails;

    public EmployeeDetailsFrame(String[] employeeDetails) {
        super("Employee Details");
        this.employeeDetails = employeeDetails;

        for (int i = 0; i < employeeDetails.length; i++) {
            fields[i].setText(employeeDetails[i]);
            fields[i].setEditable(false);
        }

        actionButton.setText("Compute Salary");
        actionButton.addActionListener(e -> onActionButtonClicked());
    }

    @Override
    protected void onActionButtonClicked() {
        new SalaryComputationFrame(employeeDetails).setVisible(true);
    }
}
