package application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class AddDoctorScreen {

    private Connection connection;

    public AddDoctorScreen() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Add Doctor");

        TextField fullNameField = new TextField();
        TextField specialtyField = new TextField();
        TextField phoneField = new TextField();
        TextField emailField = new TextField();
        TextField shiftStartField = new TextField();
        TextField shiftEndField = new TextField();

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> handleSubmit(fullNameField.getText(), specialtyField.getText(),
                phoneField.getText(), emailField.getText(), shiftStartField.getText(), shiftEndField.getText()));

        GridPane grid = new GridPane();
        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(fullNameField, 1, 0);
        grid.add(new Label("Specialty:"), 0, 1);
        grid.add(specialtyField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Shift Start (HH:MM:SS):"), 0, 4); //24 hr format
        grid.add(shiftStartField, 1, 4);
        grid.add(new Label("Shift End (HH:MM:SS):"), 0, 5); //24 hr format
        grid.add(shiftEndField, 1, 5);
        grid.add(submitButton, 1, 6);
        grid.setPadding(new Insets(20));
        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }

    private void handleSubmit(String fullName, String specialty, String phone, String email, String shiftStart, String shiftEnd) {
        // Check for empty fields
        if (fullName.isEmpty() || specialty.isEmpty() || phone.isEmpty() || email.isEmpty() || shiftStart.isEmpty() || shiftEnd.isEmpty()) {
            showErrorAlert("All fields are required!");
            return;
        }

        // Validate phone number (only digits allowed)
        if (!phone.matches("\\d+")) {
            showErrorAlert("Phone number must contain only digits.");
            return;
        }

        // Validate email format
        if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showErrorAlert("Invalid email format.");
            return;
        }

        // Validate shift start and end time format (HH:MM:SS, 24-hour)
        if (!shiftStart.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$") || 
            !shiftEnd.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")) {
            showErrorAlert("Shift times must be in the format HH:MM:SS (24-hour clock).");
            return;
        }

        // Insert into database
        try {
            String query = "INSERT INTO MedicalProfessionals (FullName, Role, Specialty, PhoneNumber, Email, ShiftStart, ShiftEnd) " +
                    "VALUES (?, 'Doctor', ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, fullName);
            stmt.setString(2, specialty);
            stmt.setString(3, phone);
            stmt.setString(4, email);
            stmt.setString(5, shiftStart);
            stmt.setString(6, shiftEnd);

            int result = stmt.executeUpdate();

            if (result > 0) {
                showConfirmationAlert("Doctor added successfully!");
            } else {
                showErrorAlert("Error adding doctor.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error connecting to the database.");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
