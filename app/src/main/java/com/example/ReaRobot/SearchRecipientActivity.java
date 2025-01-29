package com.example.ReaRobot;

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

import com.example.ReaRobot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipientActivity extends AppCompatActivity {

    private EditText editTextSearchRecipient;
    private Button buttonSearchUser;
    private RecyclerView recyclerViewUsers;
    private RecyclerViewUsersAdapter recyclerViewUserAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipient);

        editTextSearchRecipient = findViewById(R.id.editTextSearchRecipient);
        buttonSearchUser = findViewById(R.id.buttonSearchUser);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        //////////////////////

        recyclerViewUsers.addOnItemTouchListener(new RecyclerViewUsersClickListener(this, recyclerViewUsers, new RecyclerViewUsersClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Récupérer le user sélectionné
                User user = userList.get(position);

                // Lancer une nouvelle activité pour afficher le profil du patient

                Intent intent = new Intent(SearchRecipientActivity.this, ContactActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                // Gérer les actions longues clic
            }
        }));

        //////////////////////

        userList = new ArrayList<>();
        recyclerViewUserAdapter = new RecyclerViewUsersAdapter(userList);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(recyclerViewUserAdapter);

        buttonSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullNameUser = editTextSearchRecipient.getText().toString().trim();
                searchUsers(fullNameUser);
            }
        });
    }

    private void searchUsers(String fullNameUser) {
        userList.clear();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        // Create a query to search for patients by full name
        Query query = databaseReference.orderByChild("fullNameUser").equalTo(fullNameUser);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the patient data
                    User user = childSnapshot.getValue(User.class);
                    userList.add(user);
                }

                // Update the list with the search results
                recyclerViewUserAdapter.notifyDataSetChanged();

                if (userList.isEmpty()) {
                    Toast.makeText(SearchRecipientActivity.this, "No users found with the given full name", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchRecipientActivity.this, "Error occurred while searching for patients.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}