package application;

import javafx.beans.property.*;

import java.sql.Date;

public class Patient {
	 private final IntegerProperty patientID;
    private final StringProperty fullName;
    private final StringProperty phone;
    private final StringProperty emergencyContactName;
    private final StringProperty emergencyContactPhone;
    private final StringProperty symptomName;
    private final StringProperty testName;
    private final StringProperty treatmentDescription;
    private final StringProperty doctorAssigned;
    private final StringProperty admissionDetails;
    private final ObjectProperty<Date> registrationDate;

    public Patient(int patientID, String fullName, String phone, String emergencyContactName, 
            String emergencyContactPhone, String symptomName, String testName, 
            String treatmentDescription, String doctorAssigned, String admissionDetails, 
            Date registrationDate) {
 this.patientID = new SimpleIntegerProperty(patientID); 
 this.fullName = new SimpleStringProperty(fullName);
 this.phone = new SimpleStringProperty(phone);
 this.emergencyContactName = new SimpleStringProperty(emergencyContactName);
 this.emergencyContactPhone = new SimpleStringProperty(emergencyContactPhone);
 this.symptomName = new SimpleStringProperty(symptomName);
 this.testName = new SimpleStringProperty(testName);
 this.treatmentDescription = new SimpleStringProperty(treatmentDescription);
 this.doctorAssigned = new SimpleStringProperty(doctorAssigned);
 this.admissionDetails = new SimpleStringProperty(admissionDetails);
 this.registrationDate = new SimpleObjectProperty<>(registrationDate);
}

 
    public int getPatientID() {
        return patientID.get();
    }
    public StringProperty fullNameProperty() { return fullName; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty emergencyContactProperty() { return emergencyContactName; }
    public StringProperty emergencyContactPhoneProperty() { return emergencyContactPhone; }
    public StringProperty symptomProperty() { return symptomName; }
    public StringProperty testNameProperty() { return testName; }
    public StringProperty treatmentDescriptionProperty() { return treatmentDescription; }
    public StringProperty doctorAssignedProperty() { return doctorAssigned; }
    public StringProperty admissionDetailsProperty() { return admissionDetails; }
    public ObjectProperty<Date> registrationDateProperty() { return registrationDate; }
}
