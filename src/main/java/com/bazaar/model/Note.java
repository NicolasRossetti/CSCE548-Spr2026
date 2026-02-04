package com.bazaar.model;

import java.time.LocalDateTime;

public class Note {
    private int noteId;
    private int itemId;
    private String noteText;
    private LocalDateTime createdAt;

    public Note() {
    }

    public Note(int noteId, int itemId, String noteText, LocalDateTime createdAt) {
        this.noteId = noteId;
        this.itemId = itemId;
        this.noteText = noteText;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Note[id=%d, itemId=%d, text='%s', created=%s]",
                noteId, itemId, noteText, createdAt);
    }
}
