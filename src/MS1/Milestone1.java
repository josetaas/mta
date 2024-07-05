package MS1;

import javax.swing.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Milestone1 {

    public static void main(String[] args) {
        // Instantiate the login screen
        LoginScreen loginScreen = new LoginScreen();

        // Display the login screen
        loginScreen.setVisible(true);
    }
}

class LoginScreen extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JTextField employeeIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private JCheckBox keepSignedInCheckBox;
    private static final String CREDENTIALS_FILE = "/credentials.csv";

    public LoginScreen() {
        super("MotorPH Login");

        JLabel employeeIDLabel = new JLabel("Employee ID:");
        JLabel passwordLabel = new JLabel("Password:");
        employeeIDField = new JTextField(30);
        passwordField = new JPasswordField(30);
        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");
        keepSignedInCheckBox = new JCheckBox("Keep me signed in");

        loginButton.addActionListener(this);
        exitButton.addActionListener(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(employeeIDLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(employeeIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(loginButton, gbc);

        gbc.gridy = 3;
        panel.add(exitButton, gbc);

        gbc.gridy = 4;
        panel.add(keepSignedInCheckBox, gbc);

        add(panel, BorderLayout.CENTER);

        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String employeeID = employeeIDField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (isValidUser(employeeID, password)) {
                    JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + employeeID + "!");
                    dispose();

                    PayrollSystem payrollSystem = new PayrollSystem(employeeID);
                    payrollSystem.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid employee ID or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (CsvValidationException | HeadlessException | IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.getSource() == exitButton) {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    private boolean isValidUser(String employeeID, String password) throws IOException, CsvValidationException {
        try (Reader reader = new InputStreamReader(Milestone1.class.getResourceAsStream(CREDENTIALS_FILE))) {
            CSVReader csvReader = new CSVReader(reader);
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine[0].equals(employeeID) && nextLine[1].equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }
}
