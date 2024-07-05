package MS1;

import javax.swing.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;

public class EmployeeDetailsFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField monthField;
    private JTextArea employeeDetailsTextArea;
    private JButton computeButton;
    private String employeeID;
    private static final String EMPLOYEE_DATA_FILE = "/MotorPH Employee Data.csv";

    public EmployeeDetailsFrame(String employeeID) {
        super("Employee Details and Salary Computation");
        this.employeeID = employeeID;

        JLabel monthLabel = new JLabel("Enter Month:");
        monthField = new JTextField(20);
        computeButton = new JButton("Compute");
        computeButton.addActionListener(this);

        employeeDetailsTextArea = new JTextArea(20, 50);
        employeeDetailsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(employeeDetailsTextArea);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(monthLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(monthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(computeButton, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        add(panel);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == computeButton) {
            String month = monthField.getText();
            double basicSalary = 0.0;
            double riceSubsidy = 0.0;
            double phoneAllowance = 0.0;
            double clothingAllowance = 0.0;

            try (Reader reader = new InputStreamReader(Milestone1.class.getResourceAsStream(EMPLOYEE_DATA_FILE));
            	     CSVReader csvReader = new CSVReaderBuilder(reader)
            	             .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
            	             .build()) {
            	    String[] nextLine;
            	    while ((nextLine = csvReader.readNext()) != null) {
            	        if (nextLine[0].equals(employeeID)) {
            	            basicSalary = SalaryComputation.parseDoubleDefault(nextLine[13]);
            	            riceSubsidy = SalaryComputation.parseDoubleDefault(nextLine[14]);
            	            phoneAllowance = SalaryComputation.parseDoubleDefault(nextLine[15]);
            	            clothingAllowance = SalaryComputation.parseDoubleDefault(nextLine[16]);
            	            break;
            	        }
            	    }
            	} catch (IOException | CsvValidationException e1) {
            	    e1.printStackTrace();
            	}

            double totalMonthlyBaseSalary = basicSalary + riceSubsidy + phoneAllowance + clothingAllowance;
            double philhealthDeduction = SalaryComputation.calculatePhilhealthDeduction(totalMonthlyBaseSalary);
            double pagibigContribution = SalaryComputation.calculatePagibigContribution(totalMonthlyBaseSalary);
            double sssContribution = SalaryComputation.calculateSSSContribution(totalMonthlyBaseSalary);
            double withholdingTax = SalaryComputation.calculateWithholdingTax(totalMonthlyBaseSalary);

            double netSalary = totalMonthlyBaseSalary - philhealthDeduction - pagibigContribution - sssContribution - withholdingTax;

            String employeeDetails = "Employee ID: " + employeeID + "\nMonth: " + month + "\n\n";
            employeeDetails += "Base Salary: " + SalaryComputation.formatAmount(basicSalary) + "\n";
            employeeDetails += "Rice Subsidy: " + SalaryComputation.formatAmount(riceSubsidy) + "\n";
            employeeDetails += "Phone Allowance: " + SalaryComputation.formatAmount(phoneAllowance) + "\n";
            employeeDetails += "Clothing Allowance: " + SalaryComputation.formatAmount(clothingAllowance) + "\n";
            employeeDetails += "Total Monthly Base Salary: " + SalaryComputation.formatAmount(totalMonthlyBaseSalary) + "\n";
            employeeDetails += "Philhealth Deduction: " + SalaryComputation.formatAmount(philhealthDeduction) + "\n";
            employeeDetails += "Pag-ibig Contribution: " + SalaryComputation.formatAmount(pagibigContribution) + "\n";
            employeeDetails += "SSS Contribution: " + SalaryComputation.formatAmount(sssContribution) + "\n";
            employeeDetails += "Withholding Tax: " + SalaryComputation.formatAmount(withholdingTax) + "\n";
            employeeDetails += "Net Salary: " + SalaryComputation.formatAmount(netSalary) + "\n";

            employeeDetailsTextArea.setText(employeeDetails);
        }
    }
}
