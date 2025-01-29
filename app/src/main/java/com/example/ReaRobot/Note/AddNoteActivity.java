package com.example.ReaRobot.Note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ReaRobot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    private DatabaseReference notesRef;

    private EditText titleEditText, contentEditText;
    private Button addButton;
    private TextView visitNotetxt;
    private RecyclerView recyclerViewAttachments;
    private FileAttachmentAdapter attachmentAdapter;
    private List<String> fileAttachments;

    private static final int PICK_FILE_REQUEST_CODE = 101;
    private List<Uri> selectedFileUris = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        notesRef = FirebaseDatabase.getInstance().getReference("Notes");

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        visitNotetxt = findViewById(R.id.visitNotetxt);
        addButton = findViewById(R.id.addButton);

        // Initialize the list of file attachments
        fileAttachments = new ArrayList<>();

        // Other code to set up the views for note title, content, etc.

        recyclerViewAttachments = findViewById(R.id.recyclerViewAttachments);
        recyclerViewAttachments.setLayoutManager(new LinearLayoutManager(this));
        attachmentAdapter = new FileAttachmentAdapter(this, fileAttachments, new FileAttachmentAdapter.OnDeleteAttachmentClickListener() {
            @Override
            public void onDeleteAttachmentClick(int position) {
                deleteAttachment(position);
            }
        });
        recyclerViewAttachments.setAdapter(attachmentAdapter);

        Button addAttachmentButton = findViewById(R.id.addAttachmentButton);
        addAttachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the button click to add a new attachment
                openFilePicker();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();

                if (!TextUtils.isEmpty(title)) {
                    addNoteData(title, content);
                } else {
                    Toast.makeText(AddNoteActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        visitNotetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNoteActivity.this, NotesListActivity.class));
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // This allows all file types to be selected
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedFileUri = data.getData();
                selectedFileUris.add(selectedFileUri);
                updateAttachmentList();
            }
        }
    }

    private void updateAttachmentList() {
        fileAttachments.clear();
        for (Uri uri : selectedFileUris) {
            fileAttachments.add(uri.toString());
        }
        attachmentAdapter.notifyDataSetChanged();
    }

    private void deleteAttachment(int position) {
        if (position >= 0 && position < selectedFileUris.size()) {
            selectedFileUris.remove(position);
            updateAttachmentList();
        }
    }

    private void addNoteData(String title, String content) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String author = currentUser.getDisplayName();
            if (TextUtils.isEmpty(author)) {
                author = currentUser.getEmail();
            }
            // Get the current date
            Date currentDate = new Date();
            // Convert the date to text
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateText = dateFormat.format(currentDate);

            DatabaseReference noteRef = notesRef.push(); // Generate a new unique ID
            String noteId = noteRef.getKey(); // Get the generated ID
            noteRef.child(noteId);
            NoteDataManager.getInstance().setNoteId(noteId);
            Note note = new Note();
            note.setTitle(title);
            note.setDate(dateText);
            note.setAuthor(author);
            note.setContent(content);
            note.setId(noteId);
            List<String> fileAttachmentUris = new ArrayList<>();
            for (Uri uri : selectedFileUris) {
                fileAttachmentUris.add(uri.toString());
            }
            note.setFileAttachments(fileAttachmentUris);

            noteRef.setValue(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddNoteActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                            clearForm();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNoteActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case when there is no authenticated user
            // You can prompt the user to sign in or handle it as per your app's requirements
            Toast.makeText(AddNoteActivity.this, "User not authenticated. Please sign in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        titleEditText.setText("");
        contentEditText.setText("");
        selectedFileUris.clear();
        updateAttachmentList();
    }
}
