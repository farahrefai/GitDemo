package com.example.ReaRobot.Note;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ReaRobot.R;

import java.io.File;
import java.util.List;

public class NoteDetailsActivity extends AppCompatActivity {
    TextView textViewTitle, textViewContent, textViewAuthor, textViewFileAttachment;
    ImageView imageViewFileAttachment;
    Button editButton;
    LinearLayout layoutAttachment;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        // Récupérer la note passée depuis l'intent
        Note note = (Note) getIntent().getSerializableExtra("note");

        // Afficher le contenu et les fichiers joints de la note dans votre interface utilisateur
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewContent = findViewById(R.id.textViewContent);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewFileAttachment = findViewById(R.id.textViewFileAttachment);
        imageViewFileAttachment = findViewById(R.id.imageViewFileAttachment);
        layoutAttachment= findViewById(R.id.layoutAttachment);
        editButton=findViewById(R.id.editButton);

        textViewTitle.setText("Title: " + note.getTitle());
        textViewContent.setText("Content: " + note.getContent());
        textViewAuthor.setText("Author: " + note.getAuthor());

        // Display the file attachments if available
        List<String> fileAttachments = note.getFileAttachments();
        if (fileAttachments != null && !fileAttachments.isEmpty()) {
            // For demonstration purposes, let's assume we are only displaying the first attachment
            String firstAttachment = fileAttachments.get(0);
            textViewFileAttachment.setText(getFileNameFromUri(firstAttachment));
            imageViewFileAttachment.setVisibility(View.VISIBLE);
            layoutAttachment.setVisibility(View.VISIBLE);

            layoutAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Add functionality to download and open the attached file
                    // For example, you can use an Intent to open the file with appropriate apps
                    openFile(firstAttachment);
                }
            });
        } else {
            layoutAttachment.setVisibility(View.GONE);
        }
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDetails(note);
            }
        });


    }
    private String getFileNameFromUri(String uri) {
        // Implement this method to extract the file name from the file attachment URI
        // For example, you can use String manipulation or ContentResolver to get the file name
        // and return it.
        // For demonstration purposes, let's assume the URI is a direct file path.
        File file = new File(uri);
        return file.getName();
    }
    private void openFile(String fileAttachment) {
        Uri fileUri = Uri.parse(fileAttachment);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setData(fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant read permission to the URI
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Handle the case where there's no activity to open the file
            Toast.makeText(this, "No app available to open the file.", Toast.LENGTH_SHORT).show();
        }
    }



    private String getMimeType(Uri uri) {
        // Get the MIME type of the file based on its URI
        ContentResolver contentResolver = getContentResolver();
        return contentResolver.getType(uri);
    }
    private void showNoteDetails(Note note) {
        Intent intent = new Intent(NoteDetailsActivity.this, editingActivity.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }

}