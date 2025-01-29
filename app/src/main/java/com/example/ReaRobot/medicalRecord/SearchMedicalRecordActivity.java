package com.example.ReaRobot.medicalRecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ReaRobot.Patient;
import com.example.ReaRobot.RecyclerViewAdapter;
import com.example.ReaRobot.RecyclerViewClickListener;
import com.example.ReaRobot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.xdrop.fuzzywuzzy.FuzzySearch;

public class SearchMedicalRecordActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private Button buttonSearch;
    private RecyclerView recyclerViewPatients;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Patient> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medical_record);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerViewPatients = findViewById(R.id.recyclerViewPatients);

        //////////////////////

        recyclerViewPatients.addOnItemTouchListener(new RecyclerViewClickListener(this, recyclerViewPatients, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Récupérer le patient sélectionné
                Patient patient = patientList.get(position);

                // Lancer une nouvelle activité pour afficher le profil du patient
                Intent intent = new Intent(SearchMedicalRecordActivity.this, MedicalRecordDetailsActivity.class);
                intent.putExtra("patient", (Serializable) patient);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Gérer les actions longues clic
            }
        }));

        //////////////////////

        patientList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(patientList);

        recyclerViewPatients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPatients.setAdapter(recyclerViewAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = editTextSearch.getText().toString().trim();
                searchPatients(fullName);
            }
        });
    }
    private void searchPatients(String fullName) {
        patientList.clear();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientList.clear();

                final String searchName = fullName.toLowerCase(); // Declare as final or effectively final variable

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the patient data
                    Patient patient = childSnapshot.getValue(Patient.class);

                    String patientFullName = patient.getFullName();
                    patientFullName = patientFullName != null ? patientFullName.toLowerCase() : "";

                    // Calculate Levenshtein distance
                    int levenshteinDistance = calculateLevenshteinDistance(patientFullName, searchName);

                    // Calculate FuzzyWuzzy ratio
                    int fuzzyRatio = FuzzySearch.ratio(patientFullName, searchName);

                    if (patientFullName.contains(searchName) || levenshteinDistance <= 2 || fuzzyRatio >= 70) {
                        patientList.add(patient);
                    }
                }

                // Update the list with the search results
                recyclerViewAdapter.notifyDataSetChanged();

                if (patientList.isEmpty()) {
                    Toast.makeText(SearchMedicalRecordActivity.this, "No patients found with the given full name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchMedicalRecordActivity.this, "Error occurred while searching for patients.", Toast.LENGTH_SHORT).show();
            }
        });
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


}
