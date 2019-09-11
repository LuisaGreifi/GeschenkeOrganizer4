package com.example.geschenkeorganizer.database;

//todo: NEU
//https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#8
//Erstellung ViewModel

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PersonEventViewModel extends AndroidViewModel {

    private Repository repo;

    private LiveData<List<PersonEventRepresentation>> allPersonEvents;

    public PersonEventViewModel(@NonNull Application application) {
        super(application);
        repo = new Repository(application);
        allPersonEvents = repo.getAllPersonsWithEvents();
    }

    public LiveData<List<PersonEventRepresentation>> getAllPersonEvents() { return allPersonEvents; }

    //todo: weiß nicht, ob ich das brauch...vllt nochmal schöner weil noch ne Abstraktionsschicht?
    public void insertPersonEvent(final String firstName, final String lastName, final String eventName, final int eventDate) {
        repo.insertPersonEvent(firstName, lastName, eventName, eventDate); }
}
