package TA;

import javax.swing.*;
import java.awt.*;

public abstract class EmployeeDetailsBaseFrame extends JFrame {
    protected JTextField[] fields;
    protected JButton actionButton;

    public EmployeeDetailsBaseFrame(String title) {
        super(title);
        fields = new JTextField[19];

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        addEmployeeDetailsFields(panel, gbc);

        actionButton = new JButton();
        gbc.gridx = 1; // Change to column 1
        gbc.gridy = 19; // Ensure it's in the 19th row
        gbc.gridwidth = 1; // Change to occupy one cell
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Change anchor to southeast
        panel.add(actionButton, gbc);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane);

        setSize(800, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(true);
    }

    private void addEmployeeDetailsFields(JPanel panel, GridBagConstraints gbc) {
        String[] labels = {
            "Employee ID:", "Last Name:", "First Name:", "Birthday:", "Address:", "Phone Number:",
            "SSS #:", "Philhealth #:", "TIN #:", "Pag-ibig #:", "Status:", "Position:",
            "Immediate Supervisor:", "Basic Salary:", "Rice Subsidy:", "Phone Allowance:",
            "Clothing Allowance:", "Gross Semi-monthly Rate:", "Hourly Rate:"
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(label, gbc);

            fields[i] = new JTextField(20);
            fields[i].setPreferredSize(new Dimension(400, 24));
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            panel.add(fields[i], gbc);
        }
    }

    protected abstract void onActionButtonClicked();
}
