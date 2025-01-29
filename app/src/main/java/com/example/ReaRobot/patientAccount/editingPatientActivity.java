package com.example.ReaRobot.patientAccount;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class editingPatientActivity extends AppCompatActivity {
    private EditText fullNameEditText, bedIdEditText, caseEditText, birthDateEditText, nationalityEditText, addressEditText, dateOfEntryEditText, releaseDateEditText, sexEditText, phoneNumberEditText;
    private Button saveButton;
    private TextView deletePatienttxt;
    private Patient selectedPatient;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_patient);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        bedIdEditText=findViewById(R.id.bedidEditText);
        caseEditText=findViewById(R.id.caseEditText);
        birthDateEditText=findViewById(R.id.birthDateEditText);
        nationalityEditText=findViewById(R.id.nationalityEditText);
        addressEditText=findViewById(R.id.addressEditText);
        dateOfEntryEditText=findViewById(R.id.dateOfEntryEditText);
        releaseDateEditText=findViewById(R.id.releaseDateEditText);
        sexEditText=findViewById(R.id.sexEditText);
        phoneNumberEditText=findViewById(R.id.phoneNumberEditText);
        saveButton=findViewById(R.id.SaveButton);
        deletePatienttxt=findViewById(R.id.deletePatient);

        selectedPatient = (Patient) getIntent().getSerializableExtra("patient");
        showPatientEditForm(selectedPatient);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedFullName = fullNameEditText.getText().toString();
                String updatedBedId= bedIdEditText.getText().toString();
                String updatedCase= caseEditText.getText().toString();
                String updatedBirthDate= birthDateEditText.getText().toString();
                String updatedNationality= nationalityEditText.getText().toString();
                String updatedAddress = addressEditText.getText().toString();
                String updatedDateOfEntry = dateOfEntryEditText.getText().toString();
                String updatedReleaseDate = releaseDateEditText.getText().toString();
                String updatedSex= sexEditText.getText().toString();
                String updatedPhoneNumber= phoneNumberEditText.getText().toString();


                selectedPatient.setFullName(updatedFullName);
                selectedPatient.setBedId(updatedBedId);
                selectedPatient.setCas(updatedCase);
                selectedPatient.setBirthDate(updatedBirthDate);
                selectedPatient.setNationality(updatedNationality);
                selectedPatient.setAddress(updatedAddress);
                selectedPatient.setEntryDate(updatedDateOfEntry);
                selectedPatient.setReleaseDate(updatedReleaseDate);
                selectedPatient.setSex(updatedSex);
                selectedPatient.setPhoneNumber(updatedPhoneNumber);

                updatePatientInDatabase(selectedPatient);

                Toast.makeText(getApplicationContext(), "Patient updated successfully", Toast.LENGTH_SHORT).show();

                navigateBackToSearchPatient();
            }
        });
        deletePatienttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePatient();
            }
        });

    }
    private void deletePatientFromDatabase(Patient patient) {
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        DatabaseReference patientRef=patientsRef.child(patient.getPatientKey());

        patientRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editingPatientActivity.this, "Patient deleted successfully", Toast.LENGTH_SHORT).show();
                        navigateBackToSearchPatient();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editingPatientActivity.this, "Failed to delete patient", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updatePatientInDatabase(Patient patient) {
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        DatabaseReference patientRef=patientsRef.child(patient.getPatientKey());

        patientRef.setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editingPatientActivity.this, "Patient updated successfully", Toast.LENGTH_SHORT).show();
                        navigateBackToSearchPatient();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editingPatientActivity.this, "Failed to update patient", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void navigateBackToSearchPatient() {
        Intent intent = new Intent(editingPatientActivity.this, SearchPatientActivity.class);
        startActivity(intent);
        finish();
    }
    private void showPatientEditForm(Patient patient) {
        if (patient != null) {
            fullNameEditText.setText(patient.getFullName());
            bedIdEditText.setText(patient.getBedId());
            caseEditText.setText(patient.getCas());
            birthDateEditText.setText(patient.getBirthDate());
            nationalityEditText.setText(patient.getNationality());
            addressEditText.setText(patient.getAddress());
            dateOfEntryEditText.setText(patient.getEntryDate());
            releaseDateEditText.setText(patient.getReleaseDate());
            sexEditText.setText(patient.getSex());
            phoneNumberEditText.setText(patient.getPhoneNumber());

        }
    }
    private void deletePatient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this patient ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePatientFromDatabase(selectedPatient);
                Toast.makeText(getApplicationContext(), "Patient deleted successfully", Toast.LENGTH_SHORT).show();
                navigateBackToSearchPatient();
                finish();
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