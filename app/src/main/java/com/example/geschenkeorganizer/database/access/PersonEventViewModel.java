package com.example.geschenkeorganizer.database.access;

/**Google Developers Codelabs. (n.d.).
 * Create the ViewModel. Implement the ViewModel.
 * Retrieved from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#8.
 * grundlegende Erstellung ViewModel */

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.geschenkeorganizer.database.representations.PersonEventRepresentation;

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

    public void insertPersonEvent(final String firstName, final String lastName, final String eventName, final int eventDate) {
        repo.insertPersonEvent(firstName, lastName, eventName, eventDate); }
}
