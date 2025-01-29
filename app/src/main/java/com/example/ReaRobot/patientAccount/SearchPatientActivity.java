package com.example.ReaRobot.patientAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class SearchPatientActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private Button buttonSearch;
    private RecyclerView recyclerViewPatients;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Patient> patientList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);

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
                Intent intent = new Intent(SearchPatientActivity.this, PatientProfileActivity.class);
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
/*
    private void searchPatients(String fullName) {
        patientList.clear();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("patients");

        Query query = databaseReference.orderByChild("fullName").equalTo(fullName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patientList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the patient data
                    Patient patient = childSnapshot.getValue(Patient.class);
                    patientList.add(patient);
                }

                // Update the list with the search results
                recyclerViewAdapter.notifyDataSetChanged();

                if (patientList.isEmpty()) {
                    Toast.makeText(SearchPatientActivity.this, "No patients found with the given full name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchPatientActivity.this, "Error occurred while searching for patients.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    La meilleure option entre FuzzySearch.ratio de la bibliothèque FuzzyWuzzy et l'algorithme de distance de Levenshtein dépend du contexte d'utilisation et des besoins spécifiques de votre application.

L'algorithme de distance de Levenshtein mesure le nombre minimum d'opérations (insertions, suppressions, substitutions) nécessaires pour transformer une chaîne en une autre. Il est simple et efficace, mais il ne prend pas en compte les variations de longueur des chaînes et peut ne pas donner les meilleurs résultats dans certains cas.

D'autre part, FuzzySearch.ratio de FuzzyWuzzy est basé sur l'algorithme de distance de Levenshtein, mais il tient également compte de la longueur des chaînes et utilise un système de pondération pour donner des résultats de correspondance plus précis. Il attribue des poids différents aux opérations d'insertion, de suppression et de substitution en fonction de leur impact sur la correspondance. Cela peut conduire à des résultats plus précis pour des comparaisons de chaînes plus complexes.

Dans l'ensemble, si vous recherchez une correspondance approximative simple entre deux chaînes sans tenir compte de leur longueur, l'algorithme de distance de Levenshtein peut être suffisant. Cependant, si vous souhaitez obtenir des résultats plus précis en tenant compte de la longueur des chaînes, des poids différents pour les opérations et d'autres considérations spécifiques, FuzzySearch.ratio de FuzzyWuzzy peut être une meilleure option.

Il est recommandé d'expérimenter les deux approches dans votre application et de choisir celle qui donne les meilleurs résultats pour votre cas d'utilisation spécifique.
En effet, vous pouvez combiner les deux approches pour obtenir de meilleurs résultats de correspondance. Vous pouvez utiliser l'algorithme de distance de Levenshtein pour mesurer la similarité entre les chaînes et utiliser FuzzySearch.ratio de FuzzyWuzzy comme une étape supplémentaire pour affiner la correspondance.

Voici un exemple de combinaison des deux approches :

Utilisez l'algorithme de distance de Levenshtein pour calculer la distance entre deux chaînes. Vous pouvez utiliser votre implémentation existante de l'algorithme de Levenshtein ou utiliser une bibliothèque qui le fournit, comme Apache Commons Lang.

Utilisez FuzzySearch.ratio de FuzzyWuzzy pour obtenir un score de correspondance basé sur la similarité des chaînes. FuzzySearch.ratio attribue un score compris entre 0 et 100 en fonction de la similarité des chaînes.

Combinez les résultats des deux approches en utilisant un algorithme de pondération ou en appliquant des seuils. Par exemple, vous pouvez considérer que les chaînes ayant un score de correspondance supérieur à un certain seuil et une distance de Levenshtein inférieure à un autre seuil sont considérées comme une correspondance valide.

En combinant les deux approches, vous pouvez obtenir une correspondance plus précise et plus robuste, en tenant compte à la fois de la similarité globale des chaînes et des variations spécifiques liées à la distance de Levenshtein.

N'oubliez pas de mesurer et d'expérimenter les résultats dans votre cas d'utilisation spécifique pour ajuster les paramètres et seuils afin d'obtenir les meilleurs résultats possibles.

 */
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
                Toast.makeText(SearchPatientActivity.this, "No patients found with the given full name", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(SearchPatientActivity.this, "Error occurred while searching for patients.", Toast.LENGTH_SHORT).show();
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
