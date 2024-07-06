package TA;

import java.text.DecimalFormat;

public class SalaryComputation {

    public static double parseDoubleDefault(String value) {
        try {
            String cleanedString = value.replaceAll("[^\\d]", "");
            return Double.parseDouble(cleanedString);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static double calculatePhilhealthDeduction(double totalMonthlyBaseSalary) {
        return totalMonthlyBaseSalary * 0.03; // Example: 3% of the total monthly base salary
    }

    public static double calculatePagibigContribution(double totalMonthlyBaseSalary) {
        return totalMonthlyBaseSalary * 0.04; // Example: 2% of the total monthly base salary
    }

    public static double calculateSSSContribution(double totalMonthlyBaseSalary) {
        double Salary1 = 22250;
        double Salary2 = 22750;
        double Salary3 = 23250;
        double Salary4 = 23750;
        double Salary5 = 24250;
        double Salary6 = 24750;
        double SalaryDeduc1 = 1012.50;
        double SalaryDeduc2 = 1035;
        double SalaryDeduc3 = 1057.50;
        double SalaryDeduc4 = 1080;
        double SalaryDeduc5 = 1102.50;
        double SalaryDeduc6 = 1125;
        if (totalMonthlyBaseSalary >= Salary6) {
            return SalaryDeduc6;
        } else if (totalMonthlyBaseSalary >= Salary5) {
            return SalaryDeduc5;
        } else if (totalMonthlyBaseSalary >= Salary4) {
            return SalaryDeduc4;
        } else if (totalMonthlyBaseSalary >= Salary3) {
            return SalaryDeduc3;
        } else if (totalMonthlyBaseSalary >= Salary2) {
            return SalaryDeduc2;
        } else if (totalMonthlyBaseSalary >= Salary1) {
            return SalaryDeduc1;
        }
        return calculateSSSContribution(0);
    }

    public static double calculateWithholdingTax(double income) {
        if (income <= 20832) {
            return 0;
        } else if (income <= 33333) {
            return (income - 20833) * 0.20;
        } else if (income <= 66667) {
            return 2500 + (income - 33333) * 0.25;
        } else if (income <= 166667) {
            return 10833 + (income - 66667) * 0.30;
        } else if (income <= 666667) {
            return 40833.33 + (income - 166667) * 0.32;
        } else {
            return 200833.33 + (income - 666667) * 0.35;
        }
    }

    public static String formatAmount(double amount) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(amount);
    }
}
