package com.example.ReaRobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.Note.AddNoteActivity;
import com.example.ReaRobot.Utilities.Constants;
import com.example.ReaRobot.Utilities.PreferenceManager;
import com.example.ReaRobot.activities.SignInActivity;
import com.example.ReaRobot.activities.VideoConferenceActivity;
import com.example.ReaRobot.medicalRecord.AddMedicalRecordActivity;
import com.example.ReaRobot.patientAccount.AddPatientActivity;
import com.example.ReaRobot.patientAccount.SearchPatientActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class UserAccountActivity extends AppCompatActivity {
    LinearLayout createLayout, visitLayout,medicalRecordid, DateTimeLayout,contactLayout, NoteLayout;
    FirebaseAuth firebaseAuth;
    FirebaseAuth firebaseAuth1;
    private LinearLayout layoutHeader;
    private PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        createLayout=findViewById(R.id.createLayout);
        visitLayout= findViewById(R.id.visitLayout);
        medicalRecordid=findViewById(R.id.medicalRecordid);
        DateTimeLayout=findViewById(R.id.DateTimeLayout);
        contactLayout=findViewById(R.id.contactLayout);
        NoteLayout=findViewById(R.id.NoteLayout);
        layoutHeader=findViewById(R.id.layoutHeader);
        firebaseAuth = FirebaseAuth.getInstance();
        preferenceManager= new PreferenceManager(getApplicationContext());

        NoteLayout.setOnClickListener(view -> startActivity(new Intent(UserAccountActivity.this, AddNoteActivity.class)));
        createLayout.setOnClickListener(view -> startActivity(new Intent(UserAccountActivity.this, AddPatientActivity.class)));
        visitLayout.setOnClickListener(view -> startActivity(new Intent(UserAccountActivity.this, SearchPatientActivity.class )));
        medicalRecordid.setOnClickListener(view -> startActivity(new Intent(UserAccountActivity.this, AddMedicalRecordActivity.class)));
        DateTimeLayout.setOnClickListener(view -> startActivity(new Intent(UserAccountActivity.this, RecordAccessActivity.class)));
        contactLayout.setOnClickListener(view -> startActivity(new Intent(UserAccountActivity.this, VideoConferenceActivity.class)));
        layoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                sendFCMTokenToDatabase(task.getResult().getToken());
            }
        });
    }
    private void sendFCMTokenToDatabase(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference= database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> Toast.makeText(UserAccountActivity.this, "Unable to send token: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    private void signOut(){
        Toast.makeText(this, "Signing Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference= database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            preferenceManager.clearPreferences();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }).addOnFailureListener(e -> Toast.makeText(UserAccountActivity.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }
}