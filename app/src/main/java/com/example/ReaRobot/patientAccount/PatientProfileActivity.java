package com.example.ReaRobot.patientAccount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.Patient;
import com.example.ReaRobot.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class PatientProfileActivity extends AppCompatActivity {
    private TextView fullNameTextView, bedIdTextView, caseTextView, nationalityTextView, addressTextView, dateOfEntryTextView, releaseDateTextView;
    private TextView phoneNumberTextView, sexTextView;
    private TextView birthDateTextView;
    private Patient patient;
    //private TextView diagnosisTextView, treatmentTextView, medicationsTextView;
    private Button editButton;
    private FirebaseAuth firebaseAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Retrieve the references of TextViews in your layout
        fullNameTextView = findViewById(R.id.full_name_text_view);
        bedIdTextView = findViewById(R.id.bed_id_text_view);
        caseTextView = findViewById(R.id.case_text_view);
        nationalityTextView = findViewById(R.id.nationality_text_view);
        addressTextView = findViewById(R.id.address_text_view);
        dateOfEntryTextView = findViewById(R.id.date_of_entry_text_view);
        releaseDateTextView = findViewById(R.id.release_date_text_view);
        phoneNumberTextView = findViewById(R.id.phone_number_text_view);
        sexTextView = findViewById(R.id.sex_text_view);
        birthDateTextView = findViewById(R.id.birth_date_text_view);
        //diagnosisTextView = findViewById(R.id.diagnosis_text_view);
        //treatmentTextView = findViewById(R.id.treatment_text_view);
        //medicationsTextView = findViewById(R.id.medications_text_view);
        editButton = findViewById(R.id.editButton);
        firebaseAuth = FirebaseAuth.getInstance();


        // Retrieve the patient information from the intent extras
        //patient = getIntent().getParcelableExtra("patient");

        patient = (Patient) getIntent().getSerializableExtra("patient");


        // Check if patient information exists
        if (patient != null) {
            // Display the patient information in the TextViews
            fullNameTextView.setText("Full Name: " + patient.getFullName());
            bedIdTextView.setText("Bed ID: " + patient.getBedId());
            caseTextView.setText("Case: " + patient.getCas());
            nationalityTextView.setText("Nationality: " + patient.getNationality());
            addressTextView.setText("Address: " + patient.getAddress());
            dateOfEntryTextView.setText("Date of Entry: " + patient.getEntryDate());
            releaseDateTextView.setText("Release Date: " + patient.getReleaseDate());
            phoneNumberTextView.setText("Phone Number: " + patient.getPhoneNumber());
            sexTextView.setText("Sex: " + patient.getSex());
            birthDateTextView.setText("Birth Date: " + patient.getBirthDate());
            /*
            diagnosisTextView.setText("Diagnosis: " + patient.getDiagnosis());
            treatmentTextView.setText("Treatment: " + patient.getTreatment());

            // Display the medications
            List<String> medications = patient.getMedications();
            if (medications != null && !medications.isEmpty()) {
                String medicationsString = TextUtils.join(", ", medications);
                medicationsTextView.setText("Medications: " + medicationsString);
            } else {
                medicationsTextView.setText("Medications: No medications available");
            }

             */


        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPatientDetails(patient);
            }
        });

    }

    private void showPatientDetails(Patient patient) {
        Intent intent = new Intent(PatientProfileActivity.this, editingPatientActivity.class);

        intent.putExtra("patient", (Serializable) patient);

        startActivity(intent);
    }

}
