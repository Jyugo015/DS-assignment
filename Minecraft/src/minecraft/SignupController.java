/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class SignupController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    
    @FXML
    private Label usernameError;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;
    @FXML
    private Label confirmPasswordError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    protected void handleSignupButtonAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();
        
        boolean hasError = false;
        
        clearErrors();

        if (username.isEmpty()) {
            usernameError.setText("Username cannot be empty");
            hasError = true;
        }

        if (password.isEmpty()) {
            passwordError.setText("Password cannot be empty");
            hasError = true;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError.setText("Confirm Password cannot be empty");
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordError.setText("Passwords do not match");
            hasError = true;
        }

        if (email.isEmpty()) {
            emailError.setText("Email cannot be empty");
            hasError = true;
        } else if (!isValidEmail(email)) {
            emailError.setText("Invalid Email");
            hasError = true;
        }

        if (UserDatabase.getUserByEmail(email) != null) {
            emailError.setText("The email is already taken.");
            hasError = true;
        }

        if (hasError) {
            return;
        }
        
        //Password hashing(database存这个，不要存普通的password）！！
        String hashedPassword = PasswordHash.hashPassword(password);
        User newUser = new User(username, email, hashedPassword);
        boolean userAdded = UserDatabase.addUser(newUser);

        if (userAdded) {
            System.out.println("User signed up: " + username + ", " + email);
        } else {
            confirmPasswordError.setText("An error occurred during signup.");
        }
  
        //这里signup过后会切回去login，然后通过login去main scene 
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene loginScene = new Scene(root);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(loginScene);
    }

    @FXML
    protected void handleBackToLoginButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene loginScene = new Scene(root);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(loginScene);
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.endsWith(".com");
    }

    private void clearErrors() {
        usernameError.setText("");
        emailError.setText("");
        passwordError.setText("");
    }

}
