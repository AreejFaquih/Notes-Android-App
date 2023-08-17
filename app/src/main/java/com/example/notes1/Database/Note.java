package com.example.notes1.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name = "subtitle")
    private String subtitle;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name="priority")
    private String priority;

    public Note(String title, String subtitle, String date, String note,String priority) {
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.note = note;
        this.priority=priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public String getPriority() {
        return priority;
    }

    public void setId(int id) {
        this.id = id;
    }
}
