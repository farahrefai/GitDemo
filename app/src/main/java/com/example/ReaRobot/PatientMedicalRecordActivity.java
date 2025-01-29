package com.example.ReaRobot;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.R;

public class PatientMedicalRecordActivity extends AppCompatActivity {

    private TextView diagnosisTextView;
    private TextView treatmentTextView;
    private ListView medicationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_record);

        // Retrieve the references of TextViews and ListView in your layout
        diagnosisTextView = findViewById(R.id.diagnosisTextView);
        treatmentTextView = findViewById(R.id.treatmentTextView);
        medicationsListView = findViewById(R.id.medicationsListView);

        // Retrieve the medical record information from the intent extras
        String medicalRecord = getIntent().getStringExtra("medicalRecord");

        // Display the medical record information in the TextViews
        if (medicalRecord != null) {
            // Split the medical record string into diagnosis and treatment
            String[] medicalRecordParts = medicalRecord.split(";");

            if (medicalRecordParts.length >= 2) {
                String diagnosis = medicalRecordParts[0];
                String treatment = medicalRecordParts[1];

                diagnosisTextView.setText("Diagnosis: " + diagnosis);
                treatmentTextView.setText("Treatment: " + treatment);
            }
        }

        // You can populate the medicationsListView with the medications data if needed
    }
}
