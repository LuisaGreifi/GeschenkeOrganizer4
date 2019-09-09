package com.example.geschenkeorganizer.presents;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.presents.Present;

// Code von NoteTaker-Übung

public class PresentsListFragment extends ListFragment {
    //todo: NEU (ausgeklammert: Interfaces, nicht benötigt zwecks LiveData)
    /**
    private OnListItemSelectedListener mCallback;


    public interface OnListItemSelectedListener {
        public void onListItemSelected(int id);
    }
     */

    public PresentsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_presents_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateList();
    }

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

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

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
        //todo: NEU (Callback erstmal ausgeklammert)
        /**
        mCallback.onListItemSelected(clickedPresent.getId());
         */
    }

    //todo: NEU (ausgeklammert: Interface/Callback)
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