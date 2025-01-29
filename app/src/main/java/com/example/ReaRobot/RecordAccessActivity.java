package com.example.ReaRobot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecordAccessActivity extends AppCompatActivity {

    private TextView textViewDate;
    private Button buttonEnregistrerAcces;
    //private EditText fullNameEditText;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_access);

        textViewDate = findViewById(R.id.textViewDate);
        buttonEnregistrerAcces = findViewById(R.id.buttonEnregistrerAcces);
        //fullNameEditText = findViewById(R.id.fullNameEditText);

        // Obtenir une référence à la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("acces");

        buttonEnregistrerAcces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enregistrerAcces();
            }
        });
    }

    private void enregistrerAcces() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String fullName = currentUser.getDisplayName();
            if (TextUtils.isEmpty(fullName)) {
                fullName = currentUser.getEmail();
            }


            // Créer un objet Map pour stocker les valeurs
            Map<String, Object> accesData = new HashMap<>();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            String dateHeure = format.format(date);
            accesData.put("fullName", fullName);
            accesData.put("dateHeure", dateHeure);


            // Enregistrer les données d'accès dans Firebase
            String key = databaseReference.push().getKey();
            databaseReference.child(key).setValue(accesData);
            // fullNameEditText.setText("");

            // Afficher une toast avec le nom de l'utilisateur et la date/heure correspondante
            String message = fullName + " a accédé à la salle le " + dateHeure;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        }
    }
}