package com.example.geschenkeorganizer.PersonsFile;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.presents.PresentsActivity;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PersonsAddFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private OnListItemChangedListener mCallback;

    private static final int EVENTTYPE_BIRTHDAY = 0, EVENTTYPE_CHRISTMAS = 1, EVENTTYPE_ANNIVERSARY = 2, EVENTTYPE_WEDDING = 3, EVENTTYPE_VALENTINESDAY = 4, EVENTTYPE_MOTHERSDAY = 5, EVENTTYPE_FATHERSDAY = 6, EVENTTYPE_NAMEDAY = 7, EVENTTYPE_OTHER = 8;
    private String eventType, eventDate;
    private int eventDateDay, eventDateMonth;

    private EditText editText_firstName, editText_surName, editText_eventDate;
    private Spinner spinner_eventType;
    private Button button_done;

    private String textFirstName, textSurName;
    private String textSpinner;

    public interface OnListItemChangedListener {
        public void onListItemChanged();
    }

    public PersonsAddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persons_add, container, false);
        button_done = view.findViewById(R.id.button_done2);
        button_done.setOnClickListener(this);

        spinner_eventType = view.findViewById(R.id.spinner_eventType);
        initSpinner(spinner_eventType);

        editText_eventDate = view.findViewById(R.id.editText_eventDate);
        initEventDate();

        return view;
    }

    private void initSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),  R.array.eventTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v,
                                       int position, long arg3) {
                int eventTypeInt = spinner_eventType.getSelectedItemPosition();
                eventType = getEvent(eventTypeInt);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initEventDate() {
        editText_eventDate.setFocusable(false);
        editText_eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog().show();
            }
        });
    }

    private DatePickerDialog createDatePickerDialog() {
        GregorianCalendar today = new GregorianCalendar();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        eventDate = df.format(date.getTime());
        editText_eventDate.setText(eventDate);
    }

    private String getEvent(int eventTypeInt) {
        switch (eventTypeInt) {
            case EVENTTYPE_BIRTHDAY:
                eventType = "Geburtstag";
                break;
            case EVENTTYPE_CHRISTMAS:
                eventType = "Weihnachtene";
                break;
            case EVENTTYPE_ANNIVERSARY:
                eventType = "Jahrestag";
                break;
            case EVENTTYPE_WEDDING:
                eventType = "Hochzeit";
                break;
            case EVENTTYPE_VALENTINESDAY:
                eventType = "Valentinstag";
                break;
            case EVENTTYPE_MOTHERSDAY:
                eventType = "Muttertag";
                break;
            case EVENTTYPE_FATHERSDAY:
                eventType = "Vatertag";
                break;
            case EVENTTYPE_NAMEDAY:
                eventType = "Namenstag";
                break;
            case EVENTTYPE_OTHER:
                eventType = "Sonstiges";
                break;
        }
        return eventType;
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
        //todo: Informationen in Datenbank speichern (textFirstName als String, textSurName als String, eventType als String, eventDateDay als int, eventDateMonth als int
    }

    private void findViewsById() {
        editText_firstName = getView().findViewById(R.id.editText_firstName);
        editText_surName = getView().findViewById(R.id.editText_surName);
        editText_eventDate = getView().findViewById(R.id.editText_eventDate);
        spinner_eventType = getView().findViewById(R.id.spinner_eventType);
    }

    private void getInformation(View v) {
        textFirstName = editText_firstName.getText().toString();
        textSurName = editText_surName.getText().toString();

        eventDate = editText_eventDate.getText().toString();
        eventDateDay = Integer.getInteger(eventDate.substring(0,2));
        eventDateMonth = Integer.getInteger(eventDate.substring(3, 5));

        int eventTypeInt = spinner_eventType.getSelectedItemPosition();
        eventType = getEvent(eventTypeInt);
    }
}
