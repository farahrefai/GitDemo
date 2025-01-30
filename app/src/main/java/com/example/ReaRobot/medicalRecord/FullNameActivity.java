package com.example.ReaRobot.medicalRecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ReaRobot.Patient;
import com.example.ReaRobot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FullNameActivity extends AppCompatActivity {
    private EditText fullNameEditText;
    private EditText fullNameEditText1;
    private EditText fullNameEditText2;
    private String fullName = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_name);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        fullNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                fullName = fullNameEditText.getText().toString().trim();
                searchPatients(fullName);
                return true;
            }
            return false;
        });

    }
    private void searchPatients(String fullName) {
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");

        Query query = patientsRef.orderByChild("fullName");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Patient> patients = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Patient patient = snapshot.getValue(Patient.class);
                    if (isNoteMatchingSearchCriteria(patient, fullName)) {
                        patients.add(patient);
                    }
                }

                if (patients.isEmpty()) {
                    showAddNewPatientDialog(fullName);
                } else {
                    showPatientsDialog(patients);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FullNameActivity.this, "Error occurred while searching for patients.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    Si vous enregistrez un patient avec le nom complet "farah" et que vous recherchez le patient "farh", il est possible que la recherche ne renvoie pas de correspondance car les deux noms ne sont pas strictement égaux. La méthode contains ne correspondra pas aux noms qui ont des caractères supplémentaires ou manquants.

Si vous souhaitez rechercher des patients en fonction d'une correspondance partielle du nom, vous devrez utiliser une approche plus avancée, telle que l'algorithme de recherche par chaîne de caractères "Levenshtein distance" ou l'algorithme de correspondance de motifs "Fuzzy matching". Ces algorithmes permettent de rechercher des correspondances proches ou partielles entre les chaînes de caractères.

Voici un exemple d'implémentation utilisant l'algorithme de distance de Levenshtein pour rechercher des correspondances partielles :
Cet exemple utilise l'algorithme de distance de Levenshtein pour calculer la distance entre deux chaînes de caractères. Si la distance est inférieure ou égale à maxDistance (2 dans cet exemple), la correspondance est considérée comme valide.

N'oubliez pas d'ajouter ces méthodes à votre classe FullNameActivity et de les utiliser dans votre méthode searchPatients pour effectuer une recherche par correspondance partielle.

Pour combiner l'utilisation de l'algorithme de distance de Levenshtein et la méthode contains
Dans cette modification, nous vérifions d'abord si patientFullName contient fullName à l'aide de la méthode contains. Si cela est vrai, nous retournons immédiatement true, indiquant qu'il y a une correspondance.

Si la première vérification échoue, nous calculons la distance de Levenshtein entre patientFullName et fullName en utilisant la méthode calculateLevenshteinDistance. Si la distance est inférieure ou égale à maxDistance, nous retournons également true, indiquant une correspondance.

De cette manière, la recherche sera effectuée en utilisant à la fois la correspondance partielle avec contains et la distance de Levenshtein pour permettre une certaine tolérance aux erreurs de saisie.
     */

    private boolean isNoteMatchingSearchCriteria(Patient patient, String fullName) {
        String patientFullName = patient.getFullName();
        patientFullName = patientFullName != null ? patientFullName.toLowerCase() : "";
        fullName = fullName.toLowerCase();

        int maxDistance = 2; // Define the maximum allowed distance

        return patientFullName.contains(fullName) || calculateLevenshteinDistance(patientFullName, fullName) <= maxDistance;

    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }



    private void showAddNewPatientDialog(String fullName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Patients Found");
        builder.setMessage("No patients found with the given full name. Do you want to add a new patient?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User wants to add a new patient
            showDatePickerDialog();

        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // User does not want to add a new patient
            // You can add any desired logic here
            // For example, you can clear the full name field or show a message
            fullNameEditText.setText("");
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Inflate the custom title layout
        View titleView = getLayoutInflater().inflate(R.layout.dialog_title_layout, null);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String birthDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                showPhoneNumberDialog(birthDate);
            }
        }, year, month, dayOfMonth);

        // Set the custom title view
        datePickerDialog.setCustomTitle(titleView);

        datePickerDialog.show();
    }


    private void showPhoneNumberDialog(String birthDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Phone Number");
        final EditText editTextPhoneNumber = new EditText(this);
        editTextPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(editTextPhoneNumber);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String fullName = fullNameEditText.getText().toString();
                addNewPatient(fullName, birthDate, phoneNumber);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void addNewPatient(String fullName, String birthDate, String phoneNumber) {
        String newpatientId = UUID.randomUUID().toString();
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        DatabaseReference patientRef = patientsRef.push(); // Generate a unique key for the patient
        String medicalRecordId = patientRef.push().getKey();
        Intent intent = getIntent();
        String diagnosis = intent.getStringExtra("diagnosis");
        String treatment = intent.getStringExtra("treatment");
        List<String> medications = intent.getStringArrayListExtra("medications");
        Patient patient = new Patient(newpatientId, fullName, birthDate, phoneNumber, medicalRecordId, diagnosis, treatment, medications);

        patientRef.setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(FullNameActivity.this, "Medical record added to patient profile", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FullNameActivity.this, "Failed to add medical record", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPatientsDialog(List<Patient> patients) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Patients");

        // Create an array of patient names
        String[] patientNames = new String[patients.size() +1];
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            String patientName = patient.getFullName() + " (Birth Date: " + patient.getBirthDate() + ", Phone number: " + patient.getPhoneNumber() + ")";
            patientNames[i] = patientName;
        }
        patientNames[patients.size()] = "Create a new patient";
        builder.setItems(patientNames, (dialog, which) -> {
            if (which < patients.size()) {
                // Existing patient selected, associate medical record with the patient
                Patient selectedPatient = patients.get(which);
                saveMedicalRecordWithExistingPatient(selectedPatient);
            } else if (which == patients.size()) {
                // Create a new patient
                String newPatientFullName = fullNameEditText.getText().toString();
                if (!TextUtils.isEmpty(newPatientFullName)) {
                    showAddNewPatientDialog(newPatientFullName);
                }
            }
        });

        builder.setPositiveButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveMedicalRecordWithExistingPatient(Patient selectedPatient) {
        Intent intent = getIntent();
        String diagnosis = intent.getStringExtra("diagnosis");
        String treatment = intent.getStringExtra("treatment");
        List<String> medications = intent.getStringArrayListExtra("medications");

        // Retrieve the existing diagnosis and treatment from the selected patient
        String existingDiagnosis = selectedPatient.getDiagnosis();
        String existingTreatment = selectedPatient.getTreatment();
        List<String> existingMedications=  selectedPatient.getMedications();
        String updatedDiagnosis;
        String updatedTreatment;
        List<String> updatedMedications;

// Concatenate the new diagnosis and existing diagnosis with a separator
        if(existingDiagnosis != null &&!existingDiagnosis.isEmpty() ){
            updatedDiagnosis = existingDiagnosis + ", " + diagnosis;
        }else{
            updatedDiagnosis =diagnosis;
        }


// Concatenate the new treatment and existing treatment with a separator
        if (existingTreatment != null &&! existingTreatment.isEmpty()){
            updatedTreatment = existingTreatment + ", " + treatment;
        }else {
            updatedTreatment = treatment;
        }
        //
        if (existingMedications != null){
            updatedMedications = new ArrayList<>(existingMedications); // Create a new list with the existing medications
            updatedMedications.addAll(medications);
        }else {
            updatedMedications= medications;
        }



// Update the patient information with the updated diagnosis and treatment
        selectedPatient.setDiagnosis(updatedDiagnosis);
        selectedPatient.setTreatment(updatedTreatment);
        selectedPatient.setMedications(updatedMedications);



        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        DatabaseReference patientRef=patientsRef.child(selectedPatient.getPatientKey());
        //DatabaseReference patientRef = patientsRef.child(selectedPatient.getPatientId());

        // Save the updated patient data
        patientRef.setValue(selectedPatient)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(FullNameActivity.this, "Medical record added to patient profile", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(FullNameActivity.this, "Failed to add medical record", Toast.LENGTH_SHORT).show());
    }



}