package application;


import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpdatePatientScreen {

    private int patientID;  
    private Connection connection;

    public UpdatePatientScreen(int patientID) {
        this.patientID = patientID;

        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Update Patient Details");

        // updating patient details
        Label labelName = new Label("Full Name: ");
        Label labelPhone = new Label("Phone: ");
        Label labelEmergencyContact = new Label("Emergency Contact: ");
        Label labelSymptom = new Label("Symptom: ");
        Label labelTreatment = new Label("Treatment: ");
        Label labelDoctor = new Label("Doctor: ");
        Label labelTest = new Label("Test: ");
        

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Full Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter Phone Number");
        TextField emergencyContactField = new TextField();
        emergencyContactField.setPromptText("Enter Emergency Contact");

        TextField symptomField = new TextField();
        symptomField.setPromptText("Enter Symptom");

        TextField treatmentField = new TextField();
        treatmentField.setPromptText("Enter Treatment Description");

        ComboBox<String> doctorComboBox = new ComboBox<>();
        doctorComboBox.setPromptText("Select Doctor");

        TextField testField = new TextField();
        testField.setPromptText("Enter Test Name");

        // Fetch patient details from the database to populate the fields
        populatePatientDetails(nameField, phoneField, emergencyContactField, symptomField, treatmentField, doctorComboBox, testField);

        // Update Button to save changes
        Button btnSaveChanges = new Button("Save Changes");
        btnSaveChanges.setOnAction(e -> {
            String newName = nameField.getText();
            String newPhone = phoneField.getText();
            String newEmergencyContact = emergencyContactField.getText();
            String newSymptom = symptomField.getText();
            String newTreatment = treatmentField.getText();
            String newDoctor = doctorComboBox.getValue();
            String newTest = testField.getText();

            // Update patient information in the database
            updatePatientDetails(newName, newPhone, newEmergencyContact, newSymptom, newTreatment, newDoctor, newTest);
            stage.close(); // Close the update window after saving
            
            
        });

        // Cancel Button to close without saving
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> stage.close());
    
        // Layout the components
        VBox layout = new VBox(20, labelName, nameField, labelPhone, phoneField, labelEmergencyContact, emergencyContactField, labelSymptom, symptomField, labelTreatment, treatmentField, labelDoctor, doctorComboBox, labelTest, testField, btnSaveChanges, btnCancel);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.show();
    }

    private void populatePatientDetails(TextField nameField, TextField phoneField, TextField emergencyContactField, TextField symptomField, TextField treatmentField, ComboBox<String> doctorComboBox, TextField testField) {
        try {
            // Updated query to join the relevant tables and fetch required data
            String query = "SELECT p.FullName, p.PhoneNumber, p.EmergencyContactPhone, s.SymptomName, t.TreatmentDescription, ts.TestName " +
                           "FROM Patients p " +
                           "LEFT JOIN Symptoms s ON p.PatientID = s.PatientID " +
                           "LEFT JOIN Treatments t ON p.PatientID = t.PatientID " +
                           "LEFT JOIN Testing ts ON p.PatientID = ts.PatientID " +
                           "WHERE p.PatientID = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, patientID);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Populate the fields with the existing data from the database
                nameField.setText(resultSet.getString("FullName"));
                phoneField.setText(resultSet.getString("PhoneNumber"));
                emergencyContactField.setText(resultSet.getString("EmergencyContactPhone"));
                symptomField.setText(resultSet.getString("SymptomName"));
                treatmentField.setText(resultSet.getString("TreatmentDescription"));
                testField.setText(resultSet.getString("TestName"));
            }


            populateDoctors(doctorComboBox);

        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message in case of failure
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to fetch patient details");
            errorAlert.setContentText("There was an error retrieving the patient's details.");
            errorAlert.showAndWait();
        }
    }

    private void populateDoctors(ComboBox<String> doctorComboBox) {
        try {
            String query = "SELECT FullName FROM MedicalProfessionals";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            List<String> doctorNames = new ArrayList<>();
            while (resultSet.next()) {
                doctorNames.add(resultSet.getString("FullName"));
            }

            doctorComboBox.getItems().setAll(doctorNames);
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message in case of failure
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to fetch doctor list");
            errorAlert.setContentText("There was an error retrieving the list of doctors.");
            errorAlert.showAndWait();
        }
    }

    private void updatePatientDetails(String name, String phone, String emergencyContact, String symptom, String treatment, String doctor, String test) {
        try {
            // Update Patients table
            String updatePatientQuery = "UPDATE Patients SET FullName = ?, PhoneNumber = ?, EmergencyContactPhone = ? WHERE PatientID = ?";
            PreparedStatement stmtPatient = connection.prepareStatement(updatePatientQuery);
            stmtPatient.setString(1, name);
            stmtPatient.setString(2, phone);
            stmtPatient.setString(3, emergencyContact);
            stmtPatient.setInt(4, patientID);
            stmtPatient.executeUpdate();

            // Update Symptoms table
            String updateSymptomsQuery = "UPDATE Symptoms SET SymptomName = ? WHERE PatientID = ?";
            PreparedStatement stmtSymptoms = connection.prepareStatement(updateSymptomsQuery);
            stmtSymptoms.setString(1, symptom);
            stmtSymptoms.setInt(2, patientID);
            stmtSymptoms.executeUpdate();

            // Update Treatments table
            String updateTreatmentQuery = "UPDATE Treatments SET TreatmentDescription = ? WHERE PatientID = ?";
            PreparedStatement stmtTreatment = connection.prepareStatement(updateTreatmentQuery);
            stmtTreatment.setString(1, treatment);
            stmtTreatment.setInt(2, patientID);
            stmtTreatment.executeUpdate();

            // Update Testing table
            String updateTestQuery = "UPDATE Testing SET TestName = ? WHERE PatientID = ?";
            PreparedStatement stmtTest = connection.prepareStatement(updateTestQuery);
            stmtTest.setString(1, test);
            stmtTest.setInt(2, patientID);
            stmtTest.executeUpdate();

          
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Patient details updated successfully!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
     
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Update Failed");
            errorAlert.setContentText("There was an error updating the patient's details.");
            errorAlert.showAndWait();
        }
    }
}
