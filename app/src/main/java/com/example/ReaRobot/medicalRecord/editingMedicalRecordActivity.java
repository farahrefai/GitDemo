package com.example.ReaRobot.medicalRecord;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.Patient;
import com.example.ReaRobot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class editingMedicalRecordActivity extends AppCompatActivity {
    private EditText diagnosisEditText, treatmentEditText, medicationsEditText, fullNameEditText;
    private TextView deleteMedicalRecordtxt;
    private Button saveButton;
    private Patient selectedPatient;
    private Patient selectedPatient1;
    private Patient selectedPatient2;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_medical_record);

        diagnosisEditText = findViewById(R.id.DiagnosisEditText);
        treatmentEditText = findViewById(R.id.TreatmentEditText);
        medicationsEditText = findViewById(R.id.MedicationsEditText);
        fullNameEditText = findViewById(R.id.PatientFullNameEditText);
        saveButton = findViewById(R.id.SaveButton);
        deleteMedicalRecordtxt= findViewById(R.id.deleteMedicalRecordtxt);

        selectedPatient = (Patient) getIntent().getSerializableExtra("patient");
        showMedicalRecordEditForm(selectedPatient);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedFullName = fullNameEditText.getText().toString();
                String updatedDiagnosis = diagnosisEditText.getText().toString();
                String updatedTreatment = treatmentEditText.getText().toString();
                String updatedMedicationsString = medicationsEditText.getText().toString();
                List<String> updatedMedications = new ArrayList<>();

                // Split the comma-separated medications input into individual medications
                String[] medicationsArray = updatedMedicationsString.split(",");
                for (String medication : medicationsArray) {
                    updatedMedications.add(medication.trim());
                }

                selectedPatient.setFullName(updatedFullName);
                selectedPatient.setDiagnosis(updatedDiagnosis);
                selectedPatient.setTreatment(updatedTreatment);
                selectedPatient.setMedications(updatedMedications);

                updateMedicalRecordInDatabase(selectedPatient);

                Toast.makeText(getApplicationContext(), "Medical record updated successfully", Toast.LENGTH_SHORT).show();

                navigateBackToSearchMedicalRecord();
            }
        });
        deleteMedicalRecordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMedicalRecord();
            }
        });
    }
    private void deleteMedicalRecordFromDatabase(Patient patient) {
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        DatabaseReference patientRef=patientsRef.child(patient.getPatientKey());

        patientRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editingMedicalRecordActivity.this, "Medical record deleted successfully", Toast.LENGTH_SHORT).show();
                        navigateBackToSearchMedicalRecord();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editingMedicalRecordActivity.this, "Failed to delete medical record", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateMedicalRecordInDatabase(Patient patient) {
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        DatabaseReference patientRef=patientsRef.child(patient.getPatientKey());

        patientRef.setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editingMedicalRecordActivity.this, "Medical record updated successfully", Toast.LENGTH_SHORT).show();
                        navigateBackToSearchMedicalRecord();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editingMedicalRecordActivity.this, "Failed to update medical record", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void navigateBackToSearchMedicalRecord() {
        Intent intent = new Intent(editingMedicalRecordActivity.this, SearchMedicalRecordActivity.class);
        startActivity(intent);
        finish();
    }
    private void showMedicalRecordEditForm(Patient patient) {
        if (patient != null) {
            diagnosisEditText.setText(patient.getDiagnosis());
            treatmentEditText.setText(patient.getTreatment());
            medicationsEditText.setText(formatMedications(patient.getMedications()));
            fullNameEditText.setText(patient.getFullName());
        }
    }

    private String formatMedications(List<String> medications) {
        if (medications != null && !medications.isEmpty()) {
            return TextUtils.join(", ", medications);
        }
        return "";
    }
    private void deleteMedicalRecord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this medical record ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMedicalRecordFromDatabase(selectedPatient);
                Toast.makeText(getApplicationContext(), "Medical record deleted successfully", Toast.LENGTH_SHORT).show();
                navigateBackToSearchMedicalRecord();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
