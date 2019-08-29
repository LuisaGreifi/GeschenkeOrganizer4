package com.example.geschenkeorganizer.PersonsFile;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
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
import com.example.geschenkeorganizer.database.Repository;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PersonsAddFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private OnListItemChangedListener mCallback;

    private static final int EVENTTYPE_BIRTHDAY = 0, EVENTTYPE_CHRISTMAS = 1, EVENTTYPE_ANNIVERSARY = 2, EVENTTYPE_WEDDING = 3, EVENTTYPE_VALENTINESDAY = 4, EVENTTYPE_MOTHERSDAY = 5, EVENTTYPE_FATHERSDAY = 6, EVENTTYPE_NAMEDAY = 7, EVENTTYPE_OTHER = 8;
    private String eventType, eventDate;
    private int eventDateDay, eventDateMonth, eventDateInt;

    private EditText editText_firstName, editText_surName, editText_eventDate;
    private Spinner spinner_eventType;
    private Button button_done, button_calendarCall;

    private String textFirstName, textSurName;
    private String textSpinner;

    private Repository repository;

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
        button_calendarCall = view.findViewById(R.id.button_calendarCall);
        button_done.setOnClickListener(this);
        button_calendarCall.setOnClickListener(this);

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
        if(v.getId()==R.id.button_done2) {
            editText_firstName = getView().findViewById(R.id.editText_firstName2);
            editText_surName = getView().findViewById(R.id.editText_surName2);
            if (!editText_firstName.getText().toString().isEmpty() && !editText_surName.getText().toString().isEmpty()) {
                saveEntry(v);
                // todo: für Insert erstmal nicht relevant
                // mCallback.onListItemChanged();
            } else {
                Toast.makeText(getActivity(), "Du musst noch eine Person eingeben.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            //todo: Code zitieren: https://stackoverflow.com/questions/1943679/android-calendar (abgerufen am 27.08.2019)
            Intent i = new Intent();

            ComponentName cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");

            i.setComponent(cn);
            startActivity(i);
        }

    }

    private void saveEntry(View v) {
        findViewsById();
        getInformation(v);
        //todo: Informationen in Datenbank speichern (textFirstName als String, textSurName als String, eventType als String, eventDateDay als int, eventDateMonth als int

        // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Erstellung Repository mit richtigem Kontext
        // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Kontext der Activity des Fragments: Präfix: getActivity()
        repository = new Repository(getActivity().getApplicationContext());


        //todo: Informationen in Datenbank speichern (textFirstName als String, textSurName als String, eventType als String, eventDateDay als int, eventDateMonth als int
        //todo: statt Tag + Monat: 1 Int!
        //todo: NEU
        repository.insertPersonEvent(textFirstName,textSurName, eventType, eventDateInt);

        // todo: am Besten Einträge rauslöschen --> Nutzer, sieht, das gespeicehrt wurde; am besten in Post-Execute (Nicht, das Daten gelöscht werden, bevor sie gespeichert wurden)

    }

    //todo: NEU
    //todo: muss von onPostExecute aufgerufen werden..momentan nicht in Verwendung!
    //todo: damit EditText wieder leer ist, nachdem Zeug gespeichert wurde (Wenn Inserten nicht mehr funktioniert ist das schuld!)
    private void clearEditText(){
        editText_firstName.setText("");
        editText_surName.setText("");
        editText_eventDate.setText("");
        //todo: Wie Ursprungsanzeige Spinner zurückbekommen?
    }


    private void findViewsById() {
        editText_firstName = getView().findViewById(R.id.editText_firstName2);
        editText_surName = getView().findViewById(R.id.editText_surName2);
        editText_eventDate = getView().findViewById(R.id.editText_eventDate);
        spinner_eventType = getView().findViewById(R.id.spinner_eventType);
    }

    private void getInformation(View v) {
        textFirstName = editText_firstName.getText().toString();
        textSurName = editText_surName.getText().toString();

        eventDate = editText_eventDate.getText().toString();

        // https://www.journaldev.com/18361/java-remove-character-string
        // characters ersetzen + String kürzen
        eventDateInt = Integer.parseInt(eventDate.replace(".", "").substring(0, 4));

        int eventTypeInt = spinner_eventType.getSelectedItemPosition();
        eventType = getEvent(eventTypeInt);
    }
}
