package com.example.geschenkeorganizer.database;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**Google Developers Codelabs. (n.d.).
 * Create the ViewModel. Implement the ViewModel.
 * Retrieved from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#8.
 * grundlegende Erstellung ViewModel */

public class PresentViewModel extends AndroidViewModel {

    private Repository repo;

    private LiveData<List<PresentRepresentation>> allPresents;

    public PresentViewModel(@NonNull Application application) {
        super(application);
        repo = new Repository(application);
        allPresents = repo.getAllPresents();
    }

    public LiveData<List<PresentRepresentation>> getAllPresents() { return allPresents; }

    public void insertPresent(String firstName, String lastName, String eventName, String presentName, double presentPrice, String presentShop, String presentStatus) {
        repo.insertPresent(firstName, lastName, eventName, presentName, presentPrice, presentShop, presentStatus); }
}
