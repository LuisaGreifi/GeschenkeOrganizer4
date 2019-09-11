package com.example.geschenkeorganizer.presents;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.MyDatabase;
import com.example.geschenkeorganizer.database.Present;
import com.example.geschenkeorganizer.database.PresentListAdapter;
import com.example.geschenkeorganizer.database.PresentRepresentation;
import com.example.geschenkeorganizer.database.PresentViewModel;
import com.example.geschenkeorganizer.database.Repository;

import java.util.List;

//todo: NEU
// https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
// eigtl ganze Klasse, Platzierung Methoden an richtiger Stelle + Kontext!


public class PresentsListFragment extends Fragment {

    private PresentListAdapter presentListAdapter;
    private PresentViewModel presentViewModel;
    private Context context;

    public PresentsListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        presentListAdapter = new PresentListAdapter(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo: neu
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13
        presentViewModel = ViewModelProviders.of(this).get(PresentViewModel.class);
        presentViewModel.getAllPresents().observe(this, new Observer<List<PresentRepresentation>>() {
            @Override
            public void onChanged(@Nullable final List<PresentRepresentation> presents) {
                // Update the cached copy of the words in the adapter.
                presentListAdapter.setPresents(presents);
            }


        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presents_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewPresentList);
        recyclerView.setAdapter(presentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

}