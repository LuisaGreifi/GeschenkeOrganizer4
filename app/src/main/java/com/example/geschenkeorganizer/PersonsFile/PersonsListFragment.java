package com.example.geschenkeorganizer.PersonsFile;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.geschenkeorganizer.R;

public class PersonsListFragment extends ListFragment {

    private OnListItemSelectedListener mCallback;

    public interface OnListItemSelectedListener {
        public void onListItemSelected(int id);
    }

    public PersonsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_persons_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateList();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    protected void populateList() {
        //todo: mit Datenbank verbinden (für Array)
        Person[] values = new Person[0];
        if (values.length == 0) {
            Toast.makeText(getActivity(), R.string.text_noPersons,
                    Toast.LENGTH_LONG).show();
        }
        else {
            ArrayAdapter<Person> adapter =
                    new ArrayAdapter<Person>(getActivity(), android.R.layout.simple_list_item_1,
                            values);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onListItemSelected(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (PersonsListFragment.OnListItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() + " must implement OnListItemSelectedListener!");
        }
    }
}
