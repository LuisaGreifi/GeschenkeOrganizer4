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
import com.example.geschenkeorganizer.database.PersonEventListAdapter;
import com.example.geschenkeorganizer.database.PersonEventRepresentation;
import com.example.geschenkeorganizer.database.PersonEventViewModel;
import com.example.geschenkeorganizer.database.PresentListAdapter;
import com.example.geschenkeorganizer.database.PresentRepresentation;
import com.example.geschenkeorganizer.database.PresentViewModel;

import java.util.List;

//todo: NEU
// https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
// eigtl ganze Klasse, Platzierung Methoden an richtiger Stelle + Kontext!

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
        //todo: neu
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13
        personEventViewModel = ViewModelProviders.of(this).get(PersonEventViewModel.class);
        personEventViewModel.getAllPersonEvents().observe(this, new Observer<List<PersonEventRepresentation>>() {
            @Override
            public void onChanged(@Nullable final List<PersonEventRepresentation> personEvents) {
                // Update the cached copy of the words in the adapter.
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
