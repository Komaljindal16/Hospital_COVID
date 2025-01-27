package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class AddSymptomsScreen {

    private TextField symptomNameField;
    private DatePicker startDatePicker;
    private ComboBox<String> doctorComboBox;
    private Button submitButton;

    public void show() {
        symptomNameField = new TextField();
        startDatePicker = new DatePicker();
        doctorComboBox = new ComboBox<>();
        submitButton = new Button("Submit");

    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_details", "root", "password");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT FullName FROM MedicalProfessionals WHERE Role = 'Doctor'")) {
            while (rs.next()) {
                doctorComboBox.getItems().add(rs.getString("FullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        submitButton.setOnAction(e -> handleSubmit());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("Symptom Name"), symptomNameField,
            new Label("Start Date"), startDatePicker,
            new Label("Assign Doctor"), doctorComboBox,
            submitButton
        );
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout);
        Stage stage = new Stage();
        stage.setTitle("Add Symptoms and Assign Doctor");
        stage.setScene(scene);
        stage.show();
    }

    private void handleSubmit() {
        String symptomName = symptomNameField.getText();
        Date startDate = Date.valueOf(startDatePicker.getValue());
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_details", "root", "password");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Symptoms (PatientID, SymptomName, StartDate) VALUES ((SELECT PatientID FROM Patients ORDER BY PatientID DESC LIMIT 1), ?, ?)")) {
            stmt.setString(1, symptomName);
            stmt.setDate(2, startDate);
            stmt.executeUpdate();
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Symptoms added successfully!");
            successAlert.showAndWait();

          
            openAddTestingScreen();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddTestingScreen() {
        AddTestingScreen testingScreen = new AddTestingScreen();
        testingScreen.show();
    }
}
