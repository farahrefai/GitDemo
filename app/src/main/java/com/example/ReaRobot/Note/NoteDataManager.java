package com.example.ReaRobot.Note;

public class NoteDataManager {
    private static NoteDataManager instance;
    private String noteId;

    private NoteDataManager() {
        // Constructeur privé pour empêcher l'instanciation directe
    }

    public static NoteDataManager getInstance() {
        if (instance == null) {
            instance = new NoteDataManager();
        }
        return instance;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
