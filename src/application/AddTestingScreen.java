package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class AddTestingScreen {

    private TextField testNameField, treatmentField;
    private DatePicker testDatePicker, treatmentStartDatePicker;
    private Button submitButton, checkAdmissionButton;

    public void show() {
        testNameField = new TextField();
        treatmentField = new TextField();
        testDatePicker = new DatePicker();
        treatmentStartDatePicker = new DatePicker();
        submitButton = new Button("Submit Test and Treatment");
        checkAdmissionButton = new Button("Check Admission");

        submitButton.setOnAction(e -> handleSubmit());
        checkAdmissionButton.setOnAction(e -> handleCheckAdmission());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Test Name"), testNameField,
            new Label("Test Date"), testDatePicker,
            new Label("Treatment Description"), treatmentField,
            new Label("Treatment Start Date"), treatmentStartDatePicker,
            submitButton,
            checkAdmissionButton
        );
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout);
        Stage stage = new Stage();
        stage.setTitle("Add Testing and Treatment");
        stage.setScene(scene);
        stage.show();
    }

    private void handleSubmit() {
        String testName = testNameField.getText();
        Date testDate = Date.valueOf(testDatePicker.getValue());
        String treatment = treatmentField.getText();
        Date treatmentStartDate = Date.valueOf(treatmentStartDatePicker.getValue());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_details", "root", "password");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Testing (PatientID, TestName, TestDate) VALUES ((SELECT PatientID FROM Patients ORDER BY PatientID DESC LIMIT 1), ?, ?)");
             PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO Treatments (PatientID, TreatmentDescription, TreatmentStartDate) VALUES ((SELECT PatientID FROM Patients ORDER BY PatientID DESC LIMIT 1), ?, ?)")) {
            
            // Insert Test
            stmt.setString(1, testName);
            stmt.setDate(2, testDate);
            stmt.executeUpdate();
            
            // Insert Treatment
            stmt2.setString(1, treatment);
            stmt2.setDate(2, treatmentStartDate);
            stmt2.executeUpdate();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Test and Treatment information added successfully!");
            successAlert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCheckAdmission() {
        // Prompt user whether the patient needs admission
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Admission Confirmation");
        alert.setHeaderText("Does the patient need to be admitted?");
        alert.setContentText("Click OK to admit or Cancel to skip.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                openAdmissionForm();
            }
        });
    }

    private void openAdmissionForm() {
        AdmissionFormScreen admissionFormScreen = new AdmissionFormScreen();
        admissionFormScreen.show();
    }
}
