package com.example.geschenkeorganizer.presents;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.PresentListAdapter;
import com.example.geschenkeorganizer.database.PresentRepresentation;
import com.example.geschenkeorganizer.database.PresentViewModel;
import com.example.geschenkeorganizer.presents.Present;

import java.util.List;


// Code von NoteTaker-Übung

public class PresentsListFragment extends ListFragment {
    //todo: Neu (auskommentiert)
    // private OnListItemSelectedListener mCallback;

    //todo. NEU (auskommentert)
    //todo: später initialiesieren!
    /**
    public interface OnListItemSelectedListener {
        public void onListItemSelected(int id);
    }
     */

    //todo: neu
    // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13
    private PresentViewModel presentViewModel;

    //todo: Neu
    // https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
    // vor allem: Verwendung Kontext in Fragment
    private PresentListAdapter presentListAdapter;
    private Context context;


    public PresentsListFragment() {
    }

    //todo: NEU
    //Inititalisierung Adapter + Kontext
    //https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        presentListAdapter = new PresentListAdapter(context);
    }

    //todo: NEU
    //https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
    //Initialiesierung ViewModel + Adapter setzen
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initData();
    }

    //todo: NEU
    //https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
    //Initialiesierung ViewModel + Adapter setzen
    private void initData(){
        //todo: neu
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13
        // https://developer.android.com/topic/libraries/architecture/viewmodel
        // statt ViewModelProviders.of(this) -> of.(getActivity())
        presentViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(PresentViewModel.class);

        //todo: neu
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#13
        //todo: neu
        // https://developer.android.com/topic/libraries/architecture/viewmodel
        // statt ViewModelProviders.of(this) -> of.(getActivity())
        presentViewModel.getAllPresents().observe((LifecycleOwner) getActivity(), new Observer<List<PresentRepresentation>>() {
            @Override
            public void onChanged(@Nullable final List<PresentRepresentation> presents) {
                // Update the cached copy of the words in the adapter.
                presentListAdapter.setPresents(presents);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //todo: Neu
        //https://medium.com/@tonia.tkachuk/android-app-example-using-room-database-63f7091e69af
        View view = inflater.inflate(R.layout.fragment_presents_list, container, false);
        //todo: NEU
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(presentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    //todo: Neu (auskommentiert)
    // laut Medium passiert das in onCreate
    /**
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //todo: Neu (auskommentiert)
        //populateList();

    }
    */

    //todo: Neu (auskommentiert)
    // RecyclerView regelt
    // todo: bei Länge 0 wäre das schon ganz nett

    protected void populateList() {
        //todo: mit Datenbank verbinden (für Array)

        Present[] values = new Present[0];
        if (values.length == 0) {
            Toast.makeText(getActivity(), R.string.text_noPresents,
                    Toast.LENGTH_LONG).show();
        }
        else {
            ArrayAdapter<Present> adapter =
                    new ArrayAdapter<Present>(getActivity(), android.R.layout.simple_list_item_1,
                            values);
            setListAdapter(adapter);
        }
    }

//todo: Neu (auskommentiert)
    //rauslöschen (LiveData + RecyclerView kümemrt sich drum!)

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    //todo. NEU (auskommentert)
    //todo: später initialiesieren!
    /**
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Present clickedPresent = (Present) l.getItemAtPosition(position);
        //todo: ??? evtl. Geschenk verändern lassen -> GeschenkeHinzufügen-Fragment einfügen
        Toast.makeText(getActivity(),
                "Clicked on pos: " + position + "; id: " + id,
                Toast.LENGTH_LONG).show();
        // Prevent opening the dummy entry
        if (clickedPresent.getId() == -1) {
            return;
        }
        mCallback.onListItemSelected(clickedPresent.getId());
    }
     */

//todo: NEU (auskommentiert)
    // es wird kein callBack benötigt wg LiveData und Co.
    /**
    @SuppressWarnings("deprecation")
    // The new method onAttach(Context context) doesn't exist in API level 22 and below
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnListItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement OnListItemSelectedListener!");
        }
    }
    */

}