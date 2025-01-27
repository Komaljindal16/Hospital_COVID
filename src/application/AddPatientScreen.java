package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class AddPatientScreen {

    private TextField fullNameField, phoneNumberField, addressField, emergencyContactNameField,
            emergencyContactPhoneField, relationshipField;
    private DatePicker registrationDatePicker;
    private Button submitButton;

    public void show() {
        //form for patient details
        fullNameField = new TextField();
        phoneNumberField = new TextField();
        addressField = new TextField();
        emergencyContactNameField = new TextField();
        emergencyContactPhoneField = new TextField();
        relationshipField = new TextField();
        registrationDatePicker = new DatePicker();
        submitButton = new Button("Submit");

        submitButton.setOnAction(e -> handleSubmit());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Full Name"), fullNameField,
            new Label("Phone Number"), phoneNumberField,
            new Label("Address"), addressField,
            new Label("Emergency Contact Name"), emergencyContactNameField,
            new Label("Emergency Contact Phone"), emergencyContactPhoneField,
            new Label("Relationship"), relationshipField,
            new Label("Registration Date"), registrationDatePicker,
            submitButton
        );

        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout);
        Stage stage = new Stage();
        stage.setTitle("Add Patient");
        stage.setScene(scene);
        stage.show();
    }

    private void handleSubmit() {
        // Collect values
        String fullName = fullNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String address = addressField.getText();
        String emergencyContactName = emergencyContactNameField.getText();
        String emergencyContactPhone = emergencyContactPhoneField.getText();
        String relationship = relationshipField.getText();
        Date registrationDate = Date.valueOf(registrationDatePicker.getValue());

        try {
            // Validate all fields are filled
            if (fullName.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || emergencyContactName.isEmpty() || 
                emergencyContactPhone.isEmpty() || relationship.isEmpty() || registrationDate == null) {
                
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields must be filled in.");
                alert.showAndWait();
                return; // Stop execution if validation fails
            }

            // Validate that phone numbers contain only digits
            if (!phoneNumber.matches("\\d+") || !emergencyContactPhone.matches("\\d+")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText(null);
                alert.setContentText("Phone numbers should contain only digits.");
                alert.showAndWait();
                return; // Stop execution if validation fails
            }

            // Proceed to database operation
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_details", "root", "password");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO Patients (FullName, PhoneNumber, Address, EmergencyContactName, EmergencyContactPhone, Relationship, RegistrationDate) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                
                stmt.setString(1, fullName);
                stmt.setString(2, phoneNumber);
                stmt.setString(3, address);
                stmt.setString(4, emergencyContactName);
                stmt.setString(5, emergencyContactPhone);
                stmt.setString(6, relationship);
                stmt.setDate(7, registrationDate);
                stmt.executeUpdate();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Patient added successfully!");
                successAlert.showAndWait();

                //
                openAddSymptomsScreen();
            } catch (SQLException e) {
                e.printStackTrace();

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Database Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while adding the patient. Please try again.");
                errorAlert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();

            Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
            exceptionAlert.setTitle("Error");
            exceptionAlert.setHeaderText(null);
            exceptionAlert.setContentText("An unexpected error occurred. Please check your input and try again.");
            exceptionAlert.showAndWait();
        }
    }
    private void openAddSymptomsScreen() {
        AddSymptomsScreen symptomsScreen = new AddSymptomsScreen();
        symptomsScreen.show();
    }
}
