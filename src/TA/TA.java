package TA;

import javax.swing.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class TA {

  public static void main(String[] args) {
    // Instantiate the login screen
    LoginScreen loginScreen = new LoginScreen();

    // Display the login screen
    loginScreen.setVisible(true);
  }
}
