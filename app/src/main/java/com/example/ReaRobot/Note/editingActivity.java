package com.example.ReaRobot.Note;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ReaRobot.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class editingActivity extends AppCompatActivity {
    EditText TitleEditText, ContentEditText;
    Button SaveButton, pickAttachmentButton;
    TextView DeleteNotetxt;
    Note selectedNote;
    RecyclerView attachmentRecyclerView;
    FileAttachmentAdapter attachmentAdapter;
    List<String> fileAttachments ;
    Uri attachmentUri;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 102;

    // Activity result launcher for file picker
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> handleFilePickerResult(result.getData())
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        TitleEditText = findViewById(R.id.TitleEditText);
        ContentEditText = findViewById(R.id.ContentEditText);
        SaveButton = findViewById(R.id.SaveButton);
        DeleteNotetxt = findViewById(R.id.DeleteNotetxt);
        pickAttachmentButton = findViewById(R.id.pickAttachmentButton);

        attachmentRecyclerView = findViewById(R.id.attachmentRecyclerView);

        selectedNote = (Note) getIntent().getSerializableExtra("note");

        // Initialize the list of file attachments
        fileAttachments = new ArrayList<>();

        attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attachmentAdapter = new FileAttachmentAdapter(this, fileAttachments, new FileAttachmentAdapter.OnDeleteAttachmentClickListener() {
            @Override
            public void onDeleteAttachmentClick(int position) {
                if (position >= 0 && position < fileAttachments.size()) {
                    String attachmentUriString = fileAttachments.get(position);

                    // Remove the attachment from the local list
                    fileAttachments.remove(position);
                    attachmentAdapter.notifyDataSetChanged();

                    // Remove the attachment URI from the selectedNote
                    if (selectedNote != null) {
                        List<String> attachments = selectedNote.getFileAttachments();
                        if (attachments != null) {
                            attachments.remove(attachmentUriString);
                        }

                        // Update the note in Firebase to reflect the deletion of the attachment
                       // updateNoteInDatabase(selectedNote);
                    }
                }
            }
        });
        attachmentRecyclerView.setAdapter(attachmentAdapter);


        showNoteEditForm(selectedNote);

        SaveButton.setOnClickListener(v -> {
            String updatedTitle = TitleEditText.getText().toString();
            String updatedContent = ContentEditText.getText().toString();

            selectedNote.setTitle(updatedTitle);
            selectedNote.setContent(updatedContent);

            updateNoteInDatabase(selectedNote);

            Toast.makeText(getApplicationContext(), "Note updated successfully", Toast.LENGTH_SHORT).show();

            navigateBackToNoteList();
        });

        DeleteNotetxt.setOnClickListener(view -> deleteNote());

        pickAttachmentButton.setOnClickListener(view -> openFilePicker());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showNoteEditForm(Note note) {
        if (note != null) {
            TitleEditText.setText(note.getTitle());
            ContentEditText.setText(note.getContent());

            List<String> fileAttachments = note.getFileAttachments();
            if (fileAttachments != null && !fileAttachments.isEmpty()) {
                this.fileAttachments.addAll(fileAttachments);
                attachmentAdapter.notifyDataSetChanged();

                // Set the first attachment as the current attachmentUri
                if (attachmentUri == null) {
                    attachmentUri = Uri.parse(fileAttachments.get(0));
                }
            }
        }
    }

    private void updateNoteInDatabase(Note note) {
        DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("Notes");
        DatabaseReference noteRef = notesRef.child(note.getId());

        noteRef.setValue(note)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(editingActivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                    navigateBackToNoteList();
                })
                .addOnFailureListener(e -> Toast.makeText(editingActivity.this, "Failed to update note", Toast.LENGTH_SHORT).show());
    }

    private void deleteNoteFromDatabase(Note note) {
        DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("Notes");
        DatabaseReference noteRef = notesRef.child(note.getId());

        noteRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(editingActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    navigateBackToNoteList();
                })
                .addOnFailureListener(e -> Toast.makeText(editingActivity.this, "Failed to delete note", Toast.LENGTH_SHORT).show());
    }

    private void navigateBackToNoteList() {
        Intent intent = new Intent(editingActivity.this, NotesListActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteNoteFromDatabase(selectedNote);
            Toast.makeText(getApplicationContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show();
            navigateBackToNoteList();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openFilePicker() {
        // Check if the READ_EXTERNAL_STORAGE permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If the permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            // If the permission is already granted, start the file picker activity
            startFilePicker();
        }
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the file picker activity
                startFilePicker();
            } else {
                // Permission denied, show a message or take appropriate action
                Toast.makeText(this, "Read external storage permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Start the file picker activity
    private void startFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Enable multiple file selection
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select File"));
    }

    private void handleFilePickerResult(Intent result) {
        if (result != null) {
            List<Uri> selectedUris = new ArrayList<>();
            ClipData clipData = result.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri selectedUri = clipData.getItemAt(i).getUri();
                    selectedUris.add(selectedUri);
                }
            } else {
                Uri selectedUri = result.getData();
                selectedUris.add(selectedUri);
            }

            // Initialize the list of fileAttachments for selectedNote if it's null
            if (selectedNote.getFileAttachments() == null) {
                selectedNote.setFileAttachments(new ArrayList<>());
            }

            // Update attachmentUri with the first selected URI (if any)
            if (!selectedUris.isEmpty()) {
                attachmentUri = selectedUris.get(0);
            }

            // Add the selected URIs to the list of file attachments for selectedNote
            List<String> attachmentUris = getUriStrings(selectedUris);
            selectedNote.getFileAttachments().addAll(attachmentUris);

            // Update the list of fileAttachments used by the adapter
            fileAttachments.addAll(attachmentUris);
            attachmentAdapter.notifyDataSetChanged();
        }
    }



    private List<String> getUriStrings(List<Uri> uris) {
        List<String> uriStrings = new ArrayList<>();
        for (Uri uri : uris) {
            uriStrings.add(uri.toString());
        }
        return uriStrings;
    }
}
