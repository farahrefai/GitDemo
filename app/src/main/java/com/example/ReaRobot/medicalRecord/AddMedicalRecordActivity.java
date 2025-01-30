package com.example.ReaRobot.medicalRecord;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ReaRobot.R;

import java.util.ArrayList;
import java.util.List;

public class AddMedicalRecordActivity extends AppCompatActivity {
    private EditText diagnosisEditText;
    private EditText diagnosisEditText1;
    private EditText diagnosisEditText2;
    private EditText treatmentEditText;
    private List<String> medicationsList;
    private ArrayAdapter<String> medicationsAdapter;
    private TextView visitMedicalRecordtxt;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);
        diagnosisEditText = findViewById(R.id.diagnosisEditText);
        treatmentEditText = findViewById(R.id.treatmentEditText);
        Button addMedicationButton = findViewById(R.id.addMedicationButton);
        ListView medicationsListView = findViewById(R.id.medicationsListView);
        Button saveMedicalRecordButton = findViewById(R.id.saveBtn);
        visitMedicalRecordtxt=findViewById(R.id.visitMedicalRecordtxt);


        medicationsList = new ArrayList<>();
        medicationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationsList);
        medicationsListView.setAdapter(medicationsAdapter);

        addMedicationButton.setOnClickListener(view -> addMedication());
       saveMedicalRecordButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(AddMedicalRecordActivity.this, "Please write the patient's full name ", Toast.LENGTH_SHORT).show();

               String diagnosis = diagnosisEditText.getText().toString();
               String treatment = treatmentEditText.getText().toString();
               List<String> medications = new ArrayList<>(medicationsList);

               Intent intent = new Intent(AddMedicalRecordActivity.this, FullNameActivity.class);
               intent.putExtra("diagnosis", diagnosis);
               intent.putExtra("treatment", treatment);
               intent.putStringArrayListExtra("medications", new ArrayList<>(medications));
               startActivity(intent);
               finish();
           }
       });
        visitMedicalRecordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddMedicalRecordActivity.this, SearchMedicalRecordActivity.class));

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


    
}
