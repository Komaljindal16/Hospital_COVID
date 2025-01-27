package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class AdmissionFormScreen {

    private TextField floorNumberField, roomNumberField;
    private DatePicker admissionDatePicker;
    private Button submitButton;

    public void show() {
        floorNumberField = new TextField();
        roomNumberField = new TextField();
        admissionDatePicker = new DatePicker();
        submitButton = new Button("Submit Admission");

        submitButton.setOnAction(e -> handleSubmit());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Floor Number"), floorNumberField,
            new Label("Room Number"), roomNumberField,
            new Label("Admission Date"), admissionDatePicker,
            submitButton
        );
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout);
        Stage stage = new Stage();
        stage.setTitle("Admission Form");
        stage.setScene(scene);
        stage.show();
    }

    private void handleSubmit() {
        try {
            // Collect data from the fields
            int floorNumber = Integer.parseInt(floorNumberField.getText());
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            Date admissionDate = Date.valueOf(admissionDatePicker.getValue());

            // Insert data into Admissions table
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_details", "root", "password");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO Admissions (PatientID, FloorNumber, RoomNumber, AdmissionDate) VALUES ((SELECT PatientID FROM Patients ORDER BY PatientID DESC LIMIT 1), ?, ?, ?)")) {
                stmt.setInt(1, floorNumber);
                stmt.setInt(2, roomNumber);
                stmt.setDate(3, admissionDate);
                stmt.executeUpdate();
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Addmision information added successfully!");
                successAlert.showAndWait();
                
                Platform.runLater(() -> {
                    Stage stage = (Stage) submitButton.getScene().getWindow();
                    stage.close(); 
                    openHomePage();
                
                  
                    
        
                });
            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while saving the admission details. Please try again.");
                errorAlert.showAndWait();
            }

        } catch (NumberFormatException e) {
        	 e.printStackTrace();
       
             Alert inputErrorAlert = new Alert(Alert.AlertType.ERROR);
             inputErrorAlert.setTitle("Input Error");
             inputErrorAlert.setHeaderText(null);
             inputErrorAlert.setContentText("Please enter valid floor and room numbers.");
             inputErrorAlert.showAndWait();
         }
    }
    private void openHomePage() {
        Main homePage = new Main();
        homePage.start(new Stage());
    }
}
