package com.example.ReaRobot.Note;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList,this);
        editTextSearch = findViewById(R.id.editTextSearch);

        String searchText = editTextSearch.getText().toString();
        fetchNotesAndDisplay(searchText);
        recyclerView.setAdapter(notesAdapter);


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ne rien faire avant que le texte ne change
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Appeler la méthode de recherche à chaque fois que le texte change
                String searchText = editTextSearch.getText().toString();
                fetchNotesAndDisplay(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ne rien faire après que le texte a été modifié
            }
        });

    }
    private void fetchNotesAndDisplay(final String searchText) {
        DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("Notes");

        // Query to sort notes by creation date (from newest to oldest)
        Query query = notesRef.orderByChild("date").limitToLast(100);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notesList.clear();

                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);

                    // Check if the note matches the search criteria
                    if (isNoteMatchingSearchCriteria(note, searchText)) {
                        notesList.add(note);
                    }
                }

                // Sort the notes by creation date (most recent on top)
                Collections.sort(notesList, new Comparator<Note>() {
                    @Override
                    public int compare(Note note1, Note note2) {
                        return note2.getDate().compareTo(note1.getDate());
                    }
                });

                // Update your RecyclerView Adapter here if necessary
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database access errors here
            }
        });
    }

    private boolean isNoteMatchingSearchCriteria(Note note, String searchText) {
        String title = note.getTitle();
        String content = note.getContent();
        String author = note.getAuthor();
        String date = note.getDate();

        // Handle null fields by assigning an empty string
        title = title != null ? title.toLowerCase() : "";
        content = content != null ? content.toLowerCase() : "";
        author = author != null ? author.toLowerCase() : "";
        date = date != null ? date.toLowerCase() : "";

        searchText = searchText.toLowerCase();

        return title.contains(searchText) || content.contains(searchText) || author.contains(searchText) || date.contains(searchText);
    }


    public void onNoteClick(Note note) {
        showNoteDetails(note);
    }

    private void showNoteDetails(Note note) {
        Intent intent = new Intent(NotesListActivity.this, NoteDetailsActivity.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }


}