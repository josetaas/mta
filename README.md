# Payroll System

This project contains the following files:

1. **EmployeeDetailsBaseFrame.java**: This is an abstract class that serves as the base for frames that display employee details. It contains fields for employee details and an action button.

2. **LoginScreen.java**: This class represents the login screen of the application. It validates user credentials against a CSV file.

3. **PayrollSystem.java**: This is the main class of the application. It displays a list of employees and provides options to logout, create, delete, and search employees.

4. **SalaryComputation.java**: This class contains methods for calculating various components of an employee's salary.

Please refer to the individual files for more detailed information.
# Payroll System

This project contains several Java files each with its own responsibility in the system.

## src/TA/EmployeeDetailsBaseFrame.java

This file is an abstract class that serves as the base for frames that display employee details. It contains the following methods:

- `addEmployeeDetailsFields(JPanel panel, GridBagConstraints gbc)`: Adds fields for employee details to the panel.

## src/TA/LoginScreen.java

This file contains the `LoginScreen` class which is responsible for handling user login. It contains the following methods:

- `isValidUser(String employeeID, String password)`: Checks if the user is valid.

## src/TA/PayrollSystem.java

This file contains the `PayrollSystem` class which is the main class of the system. It contains the following methods:

- `actionPerformed(ActionEvent e)`: Handles button click events.

## src/TA/SalaryComputation.java

This file contains the `SalaryComputation` class which is responsible for computing the salary of employees. It contains the following methods:

- `parseDoubleDefault(String value)`: Parses a string to a double and returns a default value if it fails.
- `calculateSSSContribution(double totalMonthlyBaseSalary)`: Calculates the SSS contribution of an employee based on their total monthly base salary.
