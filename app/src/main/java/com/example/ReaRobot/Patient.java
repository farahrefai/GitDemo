package com.example.ReaRobot;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Patient implements Parcelable, Serializable {



    private String patientKey;
    private String fullName;
    private String bedId;
    private String cas;
    private String birthDate;
    private String phoneNumber;
    private String nationality;
    private String sex;
    private String address;
    private String entryDate;
    private String releaseDate;


    private String patientId;
    private List<MedicalRecord> medicalRecords;
    private String medicalRecordId;
    private MedicalRecord medicalRecord;
    private String diagnosis, treatment, medication;

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    private List<String> medications;



    public Patient() {
        // Empty constructor required for Firebase
    }


    public Patient(String patientKey,String fullName, String bedId, String cas, String birthDate, String phoneNumber,
                   String nationality, String sex, String address, String entryDate, String releaseDate,
                   String patientId) {
        this.patientKey=patientKey;
        this.fullName = fullName;
        this.bedId = bedId;
        this.cas = cas;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.sex = sex;
        this.address = address;
        this.entryDate = entryDate;
        this.releaseDate = releaseDate;
        this.patientId = patientId;
        this.medicalRecords = new ArrayList<>();
    }
    public Patient(String patientId, String fullName, String birthDate, String phoneNumber, String medicalRecordId, String diagnosis, String treatment, List<String> medications){
        this.patientId = patientId;
        this.fullName = fullName;
        this.birthDate=birthDate;
        this.phoneNumber=phoneNumber;
        this.medicalRecordId=medicalRecordId;
        this.diagnosis=diagnosis;
        this.treatment=treatment;
        this.medications=medications != null ? medications : new ArrayList<>();
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }


    public String getMedicalRecordId() {
        return medicalRecordId;
    }
    public String getPatientKey() {
        return patientKey;
    }

    public void setPatientKey(String patientKey) {
        this.patientKey = patientKey;
    }


    public Patient(String fullName, String bedId, String cas, String birthDate, String phoneNumber, String nationality, String sex, String address, String entryDate, String releaseDate, String patientId, String diagnosis, String treatment, List<String> medications) {
        this.fullName = fullName;
        this.bedId = bedId;
        this.cas = cas;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.sex = sex;
        this.address = address;
        this.entryDate = entryDate;
        this.releaseDate = releaseDate;
        this.patientId = patientId;
        this.diagnosis=diagnosis;
        this.treatment=treatment;
        this.medications=medications;
    }
    public Patient(String fullName, String bedId, String cas, String birthDate, String phoneNumber, String nationality, String sex, String address, String entryDate, String releaseDate, String patientId, MedicalRecord medicalRecord) {
        this.fullName = fullName;
        this.bedId = bedId;
        this.cas = cas;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.sex = sex;
        this.address = address;
        this.entryDate = entryDate;
        this.releaseDate = releaseDate;
        this.patientId = patientId;
        this.medicalRecord=medicalRecord;
    }

    public Patient(String fullName, String diagnosis, String treatment, List<String> medications, String patientId){
        this.fullName = fullName;
        this.diagnosis=diagnosis;
        this.treatment=treatment;
        this.medications=medications;
        this.patientId = patientId;
    }
    public Patient(String fullName, String birthDate, String phoneNumber) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
    public  Patient(String patientId, String fullName){
        this.patientId=patientId;
        this.fullName= fullName;
    }
    public  Patient(String patientId, String fullName, String birthDate, String phoneNumber, String medicalRecordId){
        this.patientId=patientId;
        this.fullName= fullName;
        this.birthDate=birthDate;
        this.phoneNumber=phoneNumber;
        this.medicalRecordId= medicalRecordId;
    }
    public Patient(String patientKey) {
        this.patientKey = patientKey;
    }

    protected Patient(Parcel in) {
        patientKey = in.readString();
        fullName = in.readString();
        bedId = in.readString();
        cas = in.readString();
        birthDate = in.readString();
        phoneNumber = in.readString();
        nationality = in.readString();
        sex = in.readString();
        address = in.readString();
        entryDate = in.readString();
        releaseDate = in.readString();
        patientId = in.readString();
        diagnosis = in.readString();
        treatment = in.readString();
        medication =in.readString();
        medications = in.readArrayList(String.class.getClassLoader());
        medicalRecords = in.createTypedArrayList(MedicalRecord.CREATOR);
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecords.add(medicalRecord);
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patientKey);
        dest.writeString(fullName);
        dest.writeString(bedId);
        dest.writeString(cas);
        dest.writeString(birthDate);
        dest.writeString(phoneNumber);
        dest.writeString(nationality);
        dest.writeString(sex);
        dest.writeString(address);
        dest.writeString(entryDate);
        dest.writeString(releaseDate);
        dest.writeString(patientId);
        dest.writeString(diagnosis);
        dest.writeString(treatment);
        dest.writeString(medication);
        dest.writeList(medications);

        dest.writeTypedList(medicalRecords);
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId=medicalRecordId;
    }
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
