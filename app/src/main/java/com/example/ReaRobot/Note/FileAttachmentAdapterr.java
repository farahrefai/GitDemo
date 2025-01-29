package com.example.ReaRobot.Note;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ReaRobot.R;

import java.util.List;

public class FileAttachmentAdapterr extends RecyclerView.Adapter<FileAttachmentAdapterr.ViewHolder> {

    private Context context;
    private List<String> fileAttachments;
    private OnDeleteAttachmentClickListener onDeleteAttachmentClickListener;

    public FileAttachmentAdapterr(Context context, List<String> fileAttachments, OnDeleteAttachmentClickListener listener) {
        this.context = context;
        this.fileAttachments = fileAttachments;
        this.onDeleteAttachmentClickListener = listener;
    }

    public void setOnDeleteAttachmentClickListener(OnDeleteAttachmentClickListener listener) {
        onDeleteAttachmentClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_attachment, parent, false);
        return new ViewHolder(view, onDeleteAttachmentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String attachmentUriString = fileAttachments.get(position);
        Uri attachmentUri = Uri.parse(attachmentUriString);
        String attachmentName = getFileNameFromUri(attachmentUri);

        holder.attachmentNameTextView.setText(attachmentName);

        // Set onClickListener for deleteAttachmentImageView
        holder.deleteAttachmentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteAttachmentClickListener != null) {
                    onDeleteAttachmentClickListener.onDeleteAttachmentClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileAttachments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView attachmentNameTextView;
        ImageView attachmentIconImageView;
        ImageView deleteAttachmentImageView;

        public ViewHolder(@NonNull View itemView, OnDeleteAttachmentClickListener onDeleteAttachmentClickListener) {
            super(itemView);
            // Initialize views here
            attachmentNameTextView = itemView.findViewById(R.id.attachmentNameTextView);
            attachmentIconImageView = itemView.findViewById(R.id.attachmentIconImageView);
            deleteAttachmentImageView = itemView.findViewById(R.id.deleteAttachmentImageView);
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String displayName = null;
        String[] projection = {OpenableColumns.DISPLAY_NAME};
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;

        try {
            cursor = resolver.query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (columnIndex != -1) {
                    displayName = cursor.getString(columnIndex);
                } else {
                    // Column not found, fallback to using the last segment of the URI as the file name
                    displayName = uri.getLastPathSegment();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return displayName;
    }

    public interface OnDeleteAttachmentClickListener {
        void onDeleteAttachmentClick(int position);
    }
}
