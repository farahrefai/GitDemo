package com.example.ReaRobot.medicalRecord;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ReaRobot.Patient;
import com.example.ReaRobot.R;

import java.io.Serializable;
import java.util.List;

public class MedicalRecordDetailsActivity extends AppCompatActivity {
    private TextView diagnosisTextView, treatmentTextView, medicationsTextView;
    private TextView fullNameTextView;
    private Button editButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_details);

        Patient patient = (Patient) getIntent().getSerializableExtra("patient");

        diagnosisTextView = findViewById(R.id.diagnosis_text_view);
        treatmentTextView = findViewById(R.id.treatment_text_view);
        medicationsTextView = findViewById(R.id.medications_text_view);
        fullNameTextView = findViewById(R.id.full_name_text_view);
        editButton=findViewById(R.id.editButton);

        // Check if patient information exists
        if (patient != null) {
            // Display the medical record information in the TextViews
            fullNameTextView.setText("Full Name: " + patient.getFullName());
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

        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMedicalRecordDetails(patient);
            }
        });
    }
    private void showMedicalRecordDetails(Patient patient) {
        Intent intent = new Intent(MedicalRecordDetailsActivity.this, editingMedicalRecordActivity.class);

        intent.putExtra("patient", (Serializable) patient);
        //intent.putExtra("diagnosis", patient.getDiagnosis());
        //intent.putExtra("treatment", patient.getTreatment());
        //intent.putStringArrayListExtra("medications", new ArrayList<>(patient.getMedications()));
        //intent.putExtra("fullName", patient.getFullName());

        startActivity(intent);
    }

}