package com.example.ReaRobot;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MedicalRecord implements Parcelable {
    private String diagnosis;
    private String treatment;
    private String medication;
    private List<String> medications;
    private String patientId;
    private String fullName;
    private String recordId;
    private String PatientKey;

    public String getPatientKey() {
        return PatientKey;
    }

    public void setPatientKey(String patientKey) {
        PatientKey = patientKey;
    }



    public MedicalRecord() {
        // Default constructor required for Firebase
    }

    public MedicalRecord(String diagnosis, String treatment, String medication, String patientId) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medication = medication;
        this.patientId = patientId;
    }

    public MedicalRecord(String recordId, String patientId, String fullName, String diagnosis, String treatment, List<String> medications) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.fullName = fullName;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = medications;
    }

    public MedicalRecord(String recordId, String diagnosis, String treatment, List<String> medications) {
        this.recordId = recordId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = medications;
    }

    public MedicalRecord(String diagnosis, String treatment, List<String> medications) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.medications = medications;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    // Parcelable implementation

    protected MedicalRecord(Parcel in) {
        diagnosis = in.readString();
        treatment = in.readString();
        medication = in.readString();
        medications = in.createStringArrayList();
        patientId = in.readString();
        fullName = in.readString();
        recordId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(diagnosis);
        dest.writeString(treatment);
        dest.writeString(medication);
        dest.writeStringList(medications);
        dest.writeString(patientId);
        dest.writeString(fullName);
        dest.writeString(recordId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MedicalRecord> CREATOR = new Creator<MedicalRecord>() {
        @Override
        public MedicalRecord createFromParcel(Parcel in) {
            return new MedicalRecord(in);
        }

        @Override
        public MedicalRecord[] newArray(int size) {
            return new MedicalRecord[size];
        }
    };
}
