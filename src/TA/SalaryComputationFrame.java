package TA;

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

public class SalaryComputationFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> monthField;
    private JTextArea salaryDetailsTextArea;
    private JButton computeButton;
    private String employeeID;
    private static final String EMPLOYEE_DATA_FILE = "/MotorPH Employee Data.csv";

    public SalaryComputationFrame(String[] employeeDetails) {
        super("Salary Computation");
        this.employeeID = employeeDetails[0];

        JLabel monthLabel = new JLabel("Enter Month:");
        String[] months = {
          "January", "February", "March", "April",
          "May", "June", "July", "August",
          "September", "October", "November", "December"
        };
        monthField = new JComboBox<>(months);
        computeButton = new JButton("Compute");
        computeButton.addActionListener(this);

        salaryDetailsTextArea = new JTextArea(10, 40);
        salaryDetailsTextArea.setEditable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
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
        gbc.gridwidth = 2;
        JScrollPane scrollPane = new JScrollPane(salaryDetailsTextArea);
        panel.add(scrollPane, gbc);

        add(panel);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == computeButton) {
            String month = (String) monthField.getSelectedItem();
            double basicSalary = 0.0;
            double riceSubsidy = 0.0;
            double phoneAllowance = 0.0;
            double clothingAllowance = 0.0;

            try (Reader reader = new InputStreamReader(TA.class.getResourceAsStream(EMPLOYEE_DATA_FILE));
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

            String salaryDetails = "Employee ID: " + employeeID + "\nMonth: " + month + "\n\n";
            salaryDetails += formatDetail("Base Salary: ", SalaryComputation.formatAmount(basicSalary));
            salaryDetails += formatDetail("Rice Subsidy: ", SalaryComputation.formatAmount(riceSubsidy));
            salaryDetails += formatDetail("Phone Allowance: ", SalaryComputation.formatAmount(phoneAllowance));
            salaryDetails += formatDetail("Clothing Allowance: ", SalaryComputation.formatAmount(clothingAllowance));
            salaryDetails += formatDetail("Total Monthly Base Salary: ", SalaryComputation.formatAmount(totalMonthlyBaseSalary));
            salaryDetails += formatDetail("Philhealth Deduction: ", SalaryComputation.formatAmount(philhealthDeduction));
            salaryDetails += formatDetail("Pag-ibig Contribution: ", SalaryComputation.formatAmount(pagibigContribution));
            salaryDetails += formatDetail("SSS Contribution: ", SalaryComputation.formatAmount(sssContribution));
            salaryDetails += formatDetail("Withholding Tax: ", SalaryComputation.formatAmount(withholdingTax));
            salaryDetails += formatDetail("Net Salary: ", SalaryComputation.formatAmount(netSalary));

            salaryDetailsTextArea.setText(salaryDetails);
        }
    }

    private String formatDetail(String label, String value) {
        return String.format("%-30s %s%n", label, value);
    }
}
