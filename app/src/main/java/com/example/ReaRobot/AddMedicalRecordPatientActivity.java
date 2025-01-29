package com.example.ReaRobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.R;
import com.example.ReaRobot.patientAccount.AddPatientActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddMedicalRecordPatientActivity extends AppCompatActivity {
    private DatabaseReference databaseRef;
    private EditText diagnosisEditText;
    private EditText treatmentEditText;
    private EditText medicationEditText;
    private Button addMedicationButton, saveBtn;
    private ListView medicationsListView;
    private List<String> medicationsList;
    private ArrayAdapter<String> medicationsAdapter;

    private static final int REQUEST_CODE_ADD_MEDICAL_RECORD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record_patient);

        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        treatmentEditText = findViewById(R.id.treatmentEditText);
        addMedicationButton = findViewById(R.id.addMedicationButton);
        medicationEditText=findViewById(R.id.medicationEditText);
        medicationsListView=findViewById(R.id.medicationsListView);
        saveBtn = findViewById(R.id.saveBtn);

        medicationsList = new ArrayList<>();
        medicationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationsList);
        medicationsListView.setAdapter(medicationsAdapter);

        addMedicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedication();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = getIntent().getStringExtra("FULL_NAME");
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(AddMedicalRecordPatientActivity.this, "Please enter the full name", Toast.LENGTH_SHORT).show();
                    return;
                }

                String patientId = UUID.randomUUID().toString();

                String diagnosis = diagnosisEditText.getText().toString();
                String treatment = treatmentEditText.getText().toString();
                List<String> medications = medicationsList;

                // Create a new MedicalRecord object
                MedicalRecord medicalRecord = new MedicalRecord(diagnosis, treatment, medications);

                // Save the MedicalRecord under the corresponding Patient
                databaseRef = FirebaseDatabase.getInstance().getReference();
                databaseRef.child("medicalRecords").child(fullName).child(patientId).setValue(medicalRecord)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddMedicalRecordPatientActivity.this, "Medical record added successfully", Toast.LENGTH_SHORT).show();

                                // Close the activity and return the MedicalRecord as a result
                                Intent resultIntent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("MEDICAL_RECORD", medicalRecord);
                                resultIntent.putExtras(bundle);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddMedicalRecordPatientActivity.this, "Failed to add medical record", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void addMedication() {
        EditText medicationEditText = findViewById(R.id.medicationEditText);
        String medication = medicationEditText.getText().toString();

        medicationsList.add(medication);
        medicationEditText.setText("");
        medicationsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_MEDICAL_RECORD && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                MedicalRecord medicalRecord = bundle.getParcelable("MEDICAL_RECORD");
                if (medicalRecord != null) {
                    // Handle the MedicalRecord data if required
                    launchAddPatientActivity();


                }
            }
        }
    }

    private void launchAddPatientActivity() {
        Intent intent = new Intent(AddMedicalRecordPatientActivity.this, AddPatientActivity.class);

        String diagnosis = diagnosisEditText.getText().toString();
        String treatment = treatmentEditText.getText().toString();
        List<String> medications = medicationsList;

        intent.putExtra("diagnosis", diagnosis);
        intent.putExtra("treatment", treatment);
        intent.putStringArrayListExtra("medications", (ArrayList<String>) medications);

        startActivityForResult(intent, REQUEST_CODE_ADD_MEDICAL_RECORD);
    }
}
