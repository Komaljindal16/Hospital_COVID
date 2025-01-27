package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
    //buttons for homescreen
        Button btnAddPatient = new Button("Add Patient");
        Button btnAddDoctor = new Button("Add Doctor");
        Button btnViewPatients = new Button("View Patient Details");

     
        VBox root = new VBox(10);  
        root.getChildren().addAll(btnAddPatient, btnAddDoctor, btnViewPatients);

        // button actions
        btnAddPatient.setOnAction(e -> openAddPatientScreen());
        btnAddDoctor.setOnAction(e -> openAddDoctorScreen());
        btnViewPatients.setOnAction(e -> openViewPatientsScreen());
        root.setPadding(new Insets(20));
     
        Scene scene = new Scene(root, 500,500);
        primaryStage.setTitle("Hospital Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //add patient details
    private void openAddPatientScreen() {
        AddPatientScreen patientScreen = new AddPatientScreen();
        patientScreen.show();
    }

    //add doctor details
    private void openAddDoctorScreen() {
        AddDoctorScreen doctorScreen = new AddDoctorScreen();
        doctorScreen.show();
    }

    //view patient details
    private void openViewPatientsScreen() {
        ViewPatientsScreen viewPatientsScreen = new ViewPatientsScreen();
        viewPatientsScreen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
