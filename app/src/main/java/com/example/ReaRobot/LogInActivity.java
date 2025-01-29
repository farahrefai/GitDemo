package com.example.ReaRobot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private AppCompatButton loginbtn;
    private TextView ForgotPasswordtxt, SignUptxt;
    private EditText loginedt, loginPasswordedt;
    ProgressBar signInProgressBar;
    private PreferenceManager preferenceManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.loginbtn);
        ForgotPasswordtxt = findViewById(R.id.ForgotPasswordtxt);
        SignUptxt= findViewById(R.id.SignUptxt);
        loginedt = findViewById(R.id.loginedt);
        loginPasswordedt = findViewById(R.id.loginPasswordedt);
        signInProgressBar = findViewById(R.id.signInProgressBar);

        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), UserAccountActivity.class);
            startActivity(intent);
            finish();
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginedt.getText().toString();
                String pass = loginPasswordedt.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LogInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogInActivity.this, UserAccountActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        loginPasswordedt.setError("Password cannot be empty");
                    }
                } else if (email.isEmpty()) {
                    loginedt.setError("Login cannot be empty");
                } else {
                    loginedt.setError("Please enter a valid login email");
                }
            }
        });

        // Forgot Password
        ForgotPasswordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this, ForgotPasswordActivity.class));
            }
        });

        SignUptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }


}
