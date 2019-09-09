package com.example.geschenkeorganizer.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//todo: NEU
//https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#8
//Erstellung ViewModel

public class PresentViewModel extends AndroidViewModel {

    private Repository repo;

    private LiveData<List<PresentRepresentation>> allPresents;

    public PresentViewModel(@NonNull Application application) {
        super(application);
        repo = new Repository(application);
        allPresents = repo.getAllPresents();
    }

    public LiveData<List<PresentRepresentation>> getAllPresents() { return allPresents; }

    //todo: weiß nicht, ob ich das brauch...vllt nochmal schöner weil noch ne Abstraktionsschicht?
    public void insertPresent(String firstName, String lastName, String eventName, String presentName, double presentPrice, String presentShop, String presentStatus) {
        repo.insertPresent(firstName, lastName, eventName, presentName, presentPrice, presentShop, presentStatus); }
}
