package com.example.ReaRobot.Note;

import java.io.Serializable;
import java.util.List;

public class Note implements Serializable {
    private String title;
    private String date;
    private String author;
    private String content;
    private List<String> fileAttachments;
    private String id;

    public Note() {}

    public Note(String title, String date, String author, String content) {
        this.title = title;
        this.date = date;
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(List<String> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }

}
