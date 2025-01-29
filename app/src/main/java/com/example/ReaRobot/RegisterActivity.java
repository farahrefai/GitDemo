package com.example.ReaRobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.ReaRobot.Utilities.Constants;
import com.example.ReaRobot.Utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    private TextView LogIn;
    private AppCompatButton Registerbtn;
    private PreferenceManager preferenceManager;
    private ProgressBar signUpProgressBar;
    private EditText fullNameedt, emailedt, phoneNumberedt,specialityedt, passwordedt, confirmPedt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");
        preferenceManager = new PreferenceManager(this);

        LogIn = findViewById(R.id.LogIntxt);
        Registerbtn = findViewById(R.id.registerbtn);
        signUpProgressBar = findViewById(R.id.signUpProgressBar);
        fullNameedt = findViewById(R.id.fullNameedt);
        emailedt = findViewById(R.id.emailedt);
        phoneNumberedt = findViewById(R.id.phoneedt);
        specialityedt = findViewById(R.id.specialityedt);
        passwordedt = findViewById(R.id.passwordedt);
        confirmPedt = findViewById(R.id.confirmPedt);
        findViewById(R.id.imageBack).setOnClickListener(view -> onBackPressed());

        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = fullNameedt.getText().toString().trim();
                String useremail = emailedt.getText().toString().trim();
                String userphone = phoneNumberedt.getText().toString().trim();
                String speciality = specialityedt.getText().toString().trim();
                String pass = passwordedt.getText().toString().trim();
                String confpass = confirmPedt.getText().toString().trim();

                if (userName.isEmpty()) {
                    fullNameedt.setError("User Name cannot be empty");
                } else if (useremail.isEmpty()) {
                    emailedt.setError("Email cannot be empty");
                } else if (userphone.isEmpty()) {
                    phoneNumberedt.setError("Number phone cannot be empty");
                } else if (speciality.isEmpty()) {
                    specialityedt.setError("Speciality cannot be empty");
                } else if (pass.isEmpty()) {
                    passwordedt.setError("Email cannot be empty");
                } else if (!confpass.equals(pass)) {
                    confirmPedt.setError("le password n'est pas le même");
                } else if (confpass.isEmpty()) {
                    confirmPedt.setError("password cannot be empty");
                } else {
                    signUp(userName, useremail, userphone, speciality, pass);
                }
            }
        });

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            }
        });

    }
    private void signUp(String userName, String useremail, String userphone, String speciality, String pass) {
        Registerbtn.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        // Create user in Firebase Authentication
        auth.createUserWithEmailAndPassword(useremail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid = auth.getCurrentUser().getUid();
                    User user = new User(userName, useremail, userphone, speciality);

                    // Store user details in Realtime Database
                    reference.child(uid).setValue(user);

                    // Store user details in Firestore
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("users").document(uid).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Sign-up and storing user details in Firebase Firestore successful
                                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                    preferenceManager.putString(Constants.KEY_USER_ID, uid);
                                    preferenceManager.putString(Constants.KEY_FIRST_Name, userName);
                                    preferenceManager.putString(Constants.KEY_EMAIL, useremail);
                                    preferenceManager.putString(Constants.KEY_SPECIALITY, speciality);
                                    preferenceManager.putString(Constants.KEY_PHONE_NUMBER, userphone);

                                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    fullNameedt.setText("");
                                    emailedt.setText("");
                                    phoneNumberedt.setText("");
                                    specialityedt.setText("");
                                    passwordedt.setText("");
                                    confirmPedt.setText("");
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Storing user details in Firebase Firestore failed
                                    signUpProgressBar.setVisibility(View.INVISIBLE);
                                    Registerbtn.setVisibility(View.VISIBLE);
                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Creating user in Firebase Authentication failed
                    signUpProgressBar.setVisibility(View.INVISIBLE);
                    Registerbtn.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}