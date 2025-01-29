package com.example.ReaRobot;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.ReaRobot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText Emailedt;
    private AppCompatButton Sendbtn;
    private FirebaseAuth firebaseAuth;

    private boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendResetPasswordEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Error sending password reset emai", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Emailedt=findViewById(R.id.Emailedt);
        Sendbtn=findViewById(R.id.Sendbtn);
        firebaseAuth = FirebaseAuth.getInstance();

        Sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=Emailedt.getText().toString().trim();
                if(isValidEmail(email)){
                    sendResetPasswordEmail(email);
                    Emailedt.setText("");
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter a valid email address",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


