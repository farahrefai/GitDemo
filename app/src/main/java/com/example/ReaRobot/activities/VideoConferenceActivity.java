package com.example.ReaRobot.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ReaRobot.MainActivity;
import com.example.ReaRobot.R;
import com.example.ReaRobot.Utilities.Constants;
import com.example.ReaRobot.Utilities.PreferenceManager;
import com.example.ReaRobot.adapters.UsersAdapter;
import com.example.ReaRobot.listeners.UsersListener;
import com.example.ReaRobot.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoConferenceActivity extends AppCompatActivity implements UsersListener {
    private PreferenceManager preferenceManager;
    private List<User> users;
    private UsersAdapter usersAdapter;
    private TextView textErrorMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageConference;
    //private int REQUEST_CODE_BATTERY_OPTIMIZATIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_conference);

        preferenceManager= new PreferenceManager(getApplicationContext());

        imageConference=findViewById(R.id.imageConference);
        TextView textTitle= findViewById(R.id.textTitle);
        textTitle.setText(String.format(
                "%s %s",
                preferenceManager.getString(Constants.KEY_FIRST_Name),
                preferenceManager.getString(Constants.KEY_LAST_Name)
        ));

        findViewById(R.id.textSignOut).setOnClickListener(view -> signOut());





        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users, this);
        usersRecyclerView.setAdapter(usersAdapter);

        textErrorMessage= findViewById(R.id.textErrorMessage);

        swipeRefreshLayout =  findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getUsers);
        getUsers();
        //checkForBatteryOptimizations();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUsers(){
        swipeRefreshLayout.setRefreshing(true);
        FirebaseFirestore database= FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS).get().addOnCompleteListener(task -> {
            swipeRefreshLayout.setRefreshing(false);
            String myUserId = preferenceManager.getString(Constants.KEY_USER_ID);
            if (task.isSuccessful() && task.getResult() != null){
                users.clear();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    if(myUserId.equals(documentSnapshot.getId())){
                        continue;
                    }
                    User user= new User();
                    user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_Name);
                    user.lastName = documentSnapshot.getString(Constants.KEY_LAST_Name);
                    user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                    user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                    users.add(user);
                }
                if (users.size() > 0){
                    usersAdapter.notifyDataSetChanged();
                }else {
                    textErrorMessage.setText(String.format("%s", "No users available"));
                    textErrorMessage.setVisibility(View.VISIBLE);
                }
            } else {
                textErrorMessage.setText(String.format("%s", "No users available"));
                textErrorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void signOut(){
        Toast.makeText(this, "Signing Out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference= database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            preferenceManager.clearPreferences();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }).addOnFailureListener(e -> Toast.makeText(VideoConferenceActivity.this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void initiateVideoMeeting(User user) {
        if (user.token == null || user.token.trim().isEmpty()) {
            Toast.makeText(this, user.firstName + " " + user.lastName + " is not available for meeting", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent= new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "video");
            startActivity(intent);
        }
    }

    @Override
    public void initiateAudioMeeting(User user) {
        if (user.token == null || user.token.trim().isEmpty()) {
            Toast.makeText(this, user.firstName + " " + user.lastName + " is not available for meeting", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent= new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("type", "audio");
            startActivity(intent);
        }
    }
    @Override
    public void onMultipleUsersAction(Boolean isMultipleUsersSelected) {
        if (isMultipleUsersSelected){
            imageConference.setVisibility(View.VISIBLE);
            imageConference.setOnClickListener(view -> {
                Intent intent= new Intent(getApplicationContext(), OutgoingInvitationActivity.class);
                intent.putExtra("selectedUsers", new Gson().toJson(usersAdapter.getSelectedUsers()));
                intent.putExtra("Type", "video");
                intent.putExtra("isMultiple", true);
                startActivity(intent);
            });
        }else {
            imageConference.setVisibility(View.GONE);
        }
    }
    /*
    private void checkForBatteryOptimizations(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PowerManager powerManager= (PowerManager) getSystemService(POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Battery optimization is enabled. It can interrupt running background services.");
                builder.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent= new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE_BATTERY_OPTIMIZATIONS);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZATIONS){
            checkForBatteryOptimizations();
        }
    }

     */

}