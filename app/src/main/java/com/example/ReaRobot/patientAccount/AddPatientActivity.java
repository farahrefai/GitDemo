package com.example.ReaRobot.patientAccount;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.Patient;
import com.example.ReaRobot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

public class AddPatientActivity extends AppCompatActivity {



    private EditText birthDateEditText, entryDateEditText, releaseDateEditText, fullNameEditText,
            bedIdEditText, casEditText, phoneNumberEditText, nationalityEditText, sexEditText, addressEditText;

    private Button addButton;
    String patientId = UUID.randomUUID().toString();

    private static final int REQUEST_CODE_ADD_MEDICAL_RECORD = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);


        birthDateEditText = findViewById(R.id.birthDateEditText);
        entryDateEditText = findViewById(R.id.entryDateEditText);
        releaseDateEditText = findViewById(R.id.releaseDateEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        bedIdEditText = findViewById(R.id.bedIdEditText);
        casEditText = findViewById(R.id.casEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        nationalityEditText = findViewById(R.id.nationalityEditText);
        sexEditText = findViewById(R.id.sexEditText);
        addressEditText = findViewById(R.id.addressEditText);


        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString().trim();
                String bedId =bedIdEditText.getText().toString().trim();
                String cas = casEditText.getText().toString().trim();
                String birthDate=birthDateEditText.getText().toString().trim();
                String phoneNumber=phoneNumberEditText.getText().toString().trim();
                String nationality= nationalityEditText.getText().toString().trim();
                String sex= sexEditText.getText().toString().trim();
                String address= addressEditText.getText().toString().trim();
                String entryDate= entryDateEditText.getText().toString().trim();
                String releaseDate = releaseDateEditText.getText().toString().trim();
                if (fullName.isEmpty()) {
                    fullNameEditText.setError("Full Name cannot be empty");
                } else if (bedId.isEmpty()) {
                    bedIdEditText.setError("Bed id cannot be empty");

                } else if (cas.isEmpty()) {
                    casEditText.setError("Case cannot be empty");

                }else if (birthDate.isEmpty()) {
                    birthDateEditText.setError("Birth date cannot be empty");

                }else if (phoneNumber.isEmpty()) {
                    phoneNumberEditText.setError("Phone number cannot be empty");

                }else if (nationality.isEmpty()) {
                    nationalityEditText.setError("Nationality cannot be empty");

                }else if (sex.isEmpty()) {
                    sexEditText.setError("Sex cannot be empty");

                }else if (address.isEmpty()) {
                    addressEditText.setError("Address cannot be empty");

                } else if (entryDate.isEmpty()) {
                    entryDateEditText.setError("Entry date cannot be empty");

                }else if (releaseDate.isEmpty()) {
                    releaseDateEditText.setError("Release date cannot be empty");

                }else {
                    addPatientData();
                }
            }
        });

        birthDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(birthDateEditText);
            }
        });

        entryDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePickerDialog(entryDateEditText);
            }
        });

        releaseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePickerDialog(releaseDateEditText);
            }
        });

    }

    private void clearForm() {
        fullNameEditText.setText("");
        birthDateEditText.setText("");
        entryDateEditText.setText("");
        releaseDateEditText.setText("");
        bedIdEditText.setText("");
        casEditText.setText("");
        sexEditText.setText("");
        addressEditText.setText("");
        phoneNumberEditText.setText("");
        nationalityEditText.setText("");
    }

    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddPatientActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                        editText.setText(date);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void showDateTimePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddPatientActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // The date is selected and stored in the variables year, month, and dayOfMonth
                        String date = dayOfMonth + "-" + (month + 1) + "-" + year;

                        // Now, show the TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddPatientActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // The time is selected and stored in the variables hourOfDay and minute
                                        String time = hourOfDay + ":" + minute;

                                        // Combine the date and time strings and set them in the EditText
                                        String dateTime = date + " " + time;
                                        editText.setText(dateTime);
                                    }
                                }, hourOfDay, minute, true);
                        timePickerDialog.show();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }


    private void addPatientData() {
        // Retrieve the values from the EditText fields
        String fullName = fullNameEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();
        String entryDate = entryDateEditText.getText().toString();
        String releaseDate = releaseDateEditText.getText().toString();
        String bedId = bedIdEditText.getText().toString();
        String cas = casEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String nationality = nationalityEditText.getText().toString();
        String sex = sexEditText.getText().toString();
        String address = addressEditText.getText().toString();

        // Add the patient to the Firebase Realtime Database
        DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference("patients");
        DatabaseReference patientRef = patientsRef.push(); // Generate a unique key for the patient
        String patientKey = patientRef.getKey(); // Get the generated patient ID

        // Create a new Patient object
        Patient patient = new Patient(patientKey,fullName, bedId, cas, birthDate, phoneNumber, nationality, sex, address, entryDate, releaseDate, patientId);


        patientRef.setValue(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Patient added successfully
                        Toast.makeText(AddPatientActivity.this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                        clearForm();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add patient
                        Toast.makeText(AddPatientActivity.this, "Failed to add patient", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}