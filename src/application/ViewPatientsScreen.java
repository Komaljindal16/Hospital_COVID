package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class ViewPatientsScreen {

    private Connection connection;

    public ViewPatientsScreen() {
        try {
            connection = DatabaseConnection.getConnection(); // Ensure your DatabaseConnection class is correctly implemented
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
	public void show() {
        Stage stage = new Stage();
        stage.setTitle("View Patients");

        //TableView and columns
        TableView<Patient> table = new TableView<>();

        //columns for the TableView
        TableColumn<Patient, String> nameColumn = new TableColumn<>("Full Name");
        TableColumn<Patient, String> phoneColumn = new TableColumn<>("Phone Number");
        TableColumn<Patient, String> emergencyContactColumn = new TableColumn<>("Emergency Contact");
        TableColumn<Patient, String> symptomColumn = new TableColumn<>("Symptom");
        TableColumn<Patient, String> testColumn = new TableColumn<>("Test Name");
        TableColumn<Patient, String> treatmentColumn = new TableColumn<>("Treatment Description");
        TableColumn<Patient, String> doctorColumn = new TableColumn<>("Doctor Assigned");
        TableColumn<Patient, String> admissionColumn = new TableColumn<>("Admission Details");
        TableColumn<Patient, Date> registrationColumn = new TableColumn<>("Registration Date");

        // Add columns to the table
        table.getColumns().addAll(nameColumn, phoneColumn, emergencyContactColumn, symptomColumn, testColumn, treatmentColumn, doctorColumn, admissionColumn, registrationColumn);

        // Set up cell value factories
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        emergencyContactColumn.setCellValueFactory(cellData -> cellData.getValue().emergencyContactProperty());
        symptomColumn.setCellValueFactory(cellData -> cellData.getValue().symptomProperty());
        testColumn.setCellValueFactory(cellData -> cellData.getValue().testNameProperty());
        treatmentColumn.setCellValueFactory(cellData -> cellData.getValue().treatmentDescriptionProperty());
        doctorColumn.setCellValueFactory(cellData -> cellData.getValue().doctorAssignedProperty());
        admissionColumn.setCellValueFactory(cellData -> cellData.getValue().admissionDetailsProperty());
        registrationColumn.setCellValueFactory(cellData -> cellData.getValue().registrationDateProperty());

        // Load data from the database
        loadPatients(table);

        // Button to update patient details
        Button btnUpdatePatient = new Button("Update Patient Details");
        btnUpdatePatient.setOnAction(e -> {
            Patient selectedPatient = table.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
           
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Patient");
                alert.setHeaderText("Do you want to update the selected patient's details?");
                alert.setContentText("This will allow you to update treatment, symptoms, doctor assigned, test, etc.");

              
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                       
                        new UpdatePatientScreen(selectedPatient.getPatientID()).show();
                    }
                });
            } else {
                // No patient selected
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("No Patient Selected");
                errorAlert.setContentText("Please select a patient to update.");
                errorAlert.showAndWait();
            }
        });


        VBox vbox = new VBox(table, btnUpdatePatient);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void loadPatients(TableView<Patient> table) {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
        	 String query = "SELECT PatientID, FullName, PhoneNumber, EmergencyContactName, EmergencyContactPhone, " +
                     "SymptomName, TestName, TreatmentDescription, DoctorName, FloorNumber, RoomNumber, RegistrationDate " +
                     "FROM PatientDetailsView"; //view
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int patientID = rs.getInt("PatientID");
                String fullName = rs.getString("FullName");
                String phone = rs.getString("PhoneNumber");
                String emergencyContactName = rs.getString("EmergencyContactName");
                String emergencyContactPhone = rs.getString("EmergencyContactPhone");
                String symptomName = rs.getString("SymptomName");
                String testName = rs.getString("TestName");
                String treatmentDescription = rs.getString("TreatmentDescription");
                String doctorName = rs.getString("DoctorName");
                String admissionDetails = "Floor: " + rs.getInt("FloorNumber") + ", Room: " + rs.getInt("RoomNumber");
                Date registrationDate = rs.getDate("RegistrationDate");

                // Add new Patient object to the list with all the details
                patients.add(new Patient(patientID, fullName, phone, emergencyContactName, emergencyContactPhone, symptomName,
                        testName, treatmentDescription, doctorName, admissionDetails, registrationDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set data into the table
        table.getItems().setAll(patients);
    }
}
