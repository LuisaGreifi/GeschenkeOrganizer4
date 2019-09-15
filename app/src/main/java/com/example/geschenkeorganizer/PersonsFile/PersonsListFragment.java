package com.example.geschenkeorganizer.PersonsFile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.representations.PersonEventRepresentation;
import com.example.geschenkeorganizer.database.access.PersonEventViewModel;

import java.util.List;

/**Tkachuk, T. (06.04.2018).
 * Android app example using Room database.
 * Retrieved from https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af.
 * grundlegende Erstellung des Fragments zur Darstellung der RecyclerView und Anbindung an Adapter */

public class PersonsListFragment extends Fragment {

    private PersonEventListAdapter personEventListAdapter;
    private PersonEventViewModel personEventViewModel;
    private Context context;


    public PersonsListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        personEventListAdapter = new PersonEventListAdapter(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**Google Developers Codelabs. (n.d.).
         * Connect with the data.
         * Retrieved from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13.
         * LiveData der Personen- und Events-Liste überwachen;
         * Übergabe der Daten an Adapter */
        personEventViewModel = ViewModelProviders.of(this).get(PersonEventViewModel.class);
        personEventViewModel.getAllPersonEvents().observe(this, new Observer<List<PersonEventRepresentation>>() {
            @Override
            public void onChanged(@Nullable final List<PersonEventRepresentation> personEvents) {
                personEventListAdapter.setPersonEvents(personEvents);
            }


        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persons_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewPersonEventList);
        recyclerView.setAdapter(personEventListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }
}
