package com.example.ReaRobot.Note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ReaRobot.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notesList;
    private NotesListActivity notesListActivity;

    public NotesAdapter(List<Note> notesList, NotesListActivity notesListActivity ) {

        this.notesList = notesList;
        this.notesListActivity= notesListActivity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.titleTextView.setText("Title: " + note.getTitle());
        holder.dateTextView.setText("Date of creation: " + note.getDate());
        holder.authorTextView.setText("Author: " + note.getAuthor());
        holder.contentTextView.setText("Content: " + note.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesListActivity.onNoteClick(note);
            }
        });
    }
    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, dateTextView, authorTextView, contentTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }

}

