package com.example.ReaRobot.Note;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ReaRobot.R;

import java.util.List;

public class FileAttachmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FileAttachmentAdapterr adapter;
    private List<String> fileAttachments;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_attachment);

        recyclerView = findViewById(R.id.recyclerViewFileAttachments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileAttachments = getIntent().getStringArrayListExtra("fileAttachments");

        if (fileAttachments != null) {
            // Since FileAttachmentAdapter requires a Context, you need to pass this as well.
            adapter = new FileAttachmentAdapterr(this, fileAttachments, new FileAttachmentAdapterr.OnDeleteAttachmentClickListener() {
                @Override
                public void onDeleteAttachmentClick(int position) {
                    // Handle the delete attachment click if needed.
                    // You can leave this empty if you don't need to perform any action in this activity.
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No file attachments found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
