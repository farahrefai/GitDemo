package com.example.ReaRobot.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.R;
import com.example.ReaRobot.UserAccountActivity;
import com.example.ReaRobot.Utilities.Constants;
import com.example.ReaRobot.Utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {
    private EditText inputFirstName, inputLastName, inputEmail, inputPhoneNumber, inputSpeciality, inputPassword, inputConfirmPassword;
    private Button buttonSignUp;
    private ProgressBar signUpProgressBar;
    private PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.imageBack).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.textSignIn).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignInActivity.class)));


        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        inputSpeciality = findViewById(R.id.inputSpeciality);
        inputPhoneNumber= findViewById(R.id.inputPhoneNumber);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        signUpProgressBar=findViewById(R.id.signUpProgressBar);
        preferenceManager=new PreferenceManager(getApplicationContext());
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputFirstName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
                } else if (inputLastName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter last name", Toast.LENGTH_SHORT).show();
                }else if (inputSpeciality.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter speciality", Toast.LENGTH_SHORT).show();
                } else if (inputPhoneNumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }  else if (inputEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                    Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if (inputPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
                } else if (!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Password & confirm password must be same", Toast.LENGTH_SHORT).show();
                } else {
                    signUp();
                }
            }
        });

    }
    private void signUp(){
        buttonSignUp.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // L'inscription s'est faite avec succès, vous pouvez maintenant ajouter d'autres détails à Firestore.
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            addUserDetailsToFirestore(userId);
                        }
                    } else {
                        // L'inscription a échoué, afficher un message d'erreur.
                        signUpProgressBar.setVisibility(View.INVISIBLE);
                        buttonSignUp.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUpActivity.this, "Inscription échouée : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUserDetailsToFirestore(String userId) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_Name, inputFirstName.getText().toString());
        user.put(Constants.KEY_LAST_Name, inputLastName.getText().toString());
        user.put(Constants.KEY_SPECIALITY, inputSpeciality.getText().toString());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());
        user.put(Constants.KEY_PHONE_NUMBER, inputPhoneNumber.getText().toString());
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());

        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)  // Utilisez l'ID utilisateur comme ID du document dans Firestore.
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Ajout réussi, mettez à jour les préférences utilisateur et naviguez vers la prochaine activité.
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, userId);
                    preferenceManager.putString(Constants.KEY_FIRST_Name, inputFirstName.getText().toString());
                    preferenceManager.putString(Constants.KEY_LAST_Name, inputLastName.getText().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString());
                    preferenceManager.putString(Constants.KEY_SPECIALITY, inputSpeciality.getText().toString());
                    preferenceManager.putString(Constants.KEY_PASSWORD, inputPassword.getText().toString());
                    preferenceManager.putString(Constants.KEY_PHONE_NUMBER, inputPhoneNumber.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), UserAccountActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Ajout échoué, afficher un message d'erreur.
                    signUpProgressBar.setVisibility(View.INVISIBLE);
                    buttonSignUp.setVisibility(View.VISIBLE);
                    Toast.makeText(SignUpActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}