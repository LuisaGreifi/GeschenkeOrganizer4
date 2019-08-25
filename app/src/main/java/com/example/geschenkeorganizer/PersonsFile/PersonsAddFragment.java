package com.example.geschenkeorganizer.PersonsFile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.geschenkeorganizer.R;

public class PersonsAddFragment extends Fragment implements View.OnClickListener {

    private OnListItemChangedListener mCallback;

    public interface OnListItemChangedListener {
        public void onListItemChanged();
    }

    public PersonsAddFragment() {

    }

    private EditText editText_firstName, editText_surName;
    private Spinner spinner_dateOfBirth;
    private Button button_done;

    private String textFirstName, textSurName;
    private String textSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persons_add, container, false);
        button_done = view.findViewById(R.id.button_done2);
        button_done.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onClick(View v) {
        editText_firstName = getView().findViewById(R.id.editText_firstName2);
        editText_surName = getView().findViewById(R.id.editText_surName2);
        if (!editText_firstName.getText().toString().isEmpty() && !editText_surName.getText().toString().isEmpty()) {
            saveEntry(v);
            mCallback.onListItemChanged();
        } else {
            Toast.makeText(getActivity(), "Du musst noch eine Person eingeben.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEntry(View v) {
        findViewsById();
        getInformation(v);
        //todo: Informationen in Datenbank speichern
    }

    private void findViewsById() {
        editText_firstName = getView().findViewById(R.id.editText_firstName);
        editText_surName = getView().findViewById(R.id.editText_surName);
        //todo: Spinner finden
    }

    private void getInformation(View v) {
        textFirstName = editText_firstName.getText().toString();
        textSurName = editText_surName.getText().toString();
       //todo: Spinner auslesen
    }
}
