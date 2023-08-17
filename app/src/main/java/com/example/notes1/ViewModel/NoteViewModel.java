package com.example.notes1.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notes1.Database.Note;
import com.example.notes1.Repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;
    private LiveData<List<Note>> hightoLow;
    private LiveData<List<Note>> lowtoHigh;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
        hightoLow = repository.getHightoLow();
        lowtoHigh = repository.getLowtoHigh();

    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getHightoLow() {
        return hightoLow;
    }

    public LiveData<List<Note>> getLowtoHigh() {
        return lowtoHigh;
    }
}
