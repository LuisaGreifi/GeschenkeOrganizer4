package com.example.geschenkeorganizer.PersonsFile;

//todo: NEU
// import androidx Fragment!
import androidx.fragment.app.Fragment;
//import android.app.Fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.example.geschenkeorganizer.MainActivity;
import com.example.geschenkeorganizer.NotifyService;
import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.Repository;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

//todo: neu Interface
public class PersonsAddFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, PersonsAddListener{

    private OnListItemChangedListener mCallback;

    // private static final int EVENTTYPE_BIRTHDAY = 0, EVENTTYPE_CHRISTMAS = 1, EVENTTYPE_ANNIVERSARY = 2, EVENTTYPE_WEDDING = 3, EVENTTYPE_VALENTINESDAY = 4, EVENTTYPE_MOTHERSDAY = 5, EVENTTYPE_FATHERSDAY = 6, EVENTTYPE_NAMEDAY = 7, EVENTTYPE_OTHER = 8;
    private String eventType, eventDate;
    private int eventDateDay, eventDateMonth, eventDateInt;

    private EditText editText_firstName, editText_surName, editText_eventDate, editText_eventType;
    //private Spinner spinner_eventType;
    private Button button_done, button_calendarCall;

    private String textFirstName, textSurName;
    private String textSpinner;

    private Repository repository;

    //todo: Neu
    //Konstante, die gesetzt wird, wenn Klick auf ListItem stattfindet und PersonsAddFragment deswegen angepasst werden soll
    //initial: add
    private int personsAddFragmentStatus = STATUS_ADD;
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;

    // todo: Neu
    //Inhalte, die in Item gespeichert sind --> sollen angezeigt/updatebar sein
    private String personFirstNameToUpdate,personLastNameToUpdate, eventNameToUpdate, eventDateToUpdateString;
    private int eventDateToUpdateInt;


    private final static int NOTIFICATION_ID = 0;
    private final static String NOTIFICATION_CHANNEL_NAME = "CH0";
    private final static String NOTIFICATION_CHANNEL_ID = "0";
    public static final int INTENT_ITEM_SELECTED_ID = 0;
    public static final String INTENT_ITEM_SELECTED_NAME = "IntentForLoading";



    public interface OnListItemChangedListener {
        public void onListItemChanged();
    }

    public PersonsAddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persons_add, container, false);

        //todo: NEU Methode findViewById -> alle auf einmal
        findViewsById(view);
        /** in findViewsById
        button_done = view.findViewById(R.id.button_done2);
        button_calendarCall = view.findViewById(R.id.button_calendarCall);
         */

        //TextViews nur setten, wenn davor listItem angeklickt wurde
        if(personsAddFragmentStatus == STATUS_UPDATE){
            setInformation();
        }

        button_done.setOnClickListener(this);
        button_calendarCall.setOnClickListener(this);

        //spinner_eventType = view.findViewById(R.id.spinner_eventType);
        //initSpinner(spinner_eventType);
/** in findViewsById
        editText_eventDate = view.findViewById(R.id.editText_eventDate);
 */
        initEventDate();

        //todo: neu
        /**Murthy, A. (04.05.2018).
         * 5 steps to implement Room persistence library in Android.
         * Retrieved from https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24.
         * Erstellung Repository mit richtigem Kontext;
         * Kontext der Activity des Fragments: Präfix: getActivity() */
        repository = new Repository(getActivity().getApplicationContext());

        //todo: NEu
        repository.setPersonsAddListener(this);

        return view;
    }

    /** private void initSpinner(Spinner spinner) {
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
     */

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

    /** private String getEvent(int eventTypeInt) {
        switch (eventTypeInt) {
            case EVENTTYPE_BIRTHDAY:
                eventType = "Geburtstag";
                break;
            case EVENTTYPE_CHRISTMAS:
                eventType = "Weihnachten";
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
     */


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_done2) {
            //todo: Neu: denk das kann man rauslöschen...oben in findViewsById
            /**
             editText_firstName = getView().findViewById(R.id.editText_firstName2);
             editText_surName = getView().findViewById(R.id.editText_surName2);
             */

            //todo:NEU
            //das hier, nicht in save + update extra
            getInformation();

            //todo: NEU
            //Unterscheidung, ob Geschenk hinzugefügt oder geupdatet wird
            if (personsAddFragmentStatus == STATUS_ADD){
                if (!editText_firstName.getText().toString().isEmpty() && !editText_surName.getText().toString().isEmpty()) {
                    createNotification("Überlege dir ein Geschenk ;-)", "Geschenke-Erinnerung");
                    saveEntry(v);
                    // todo: für Insert erstmal nicht relevant
                    //mCallback.onListItemChanged();
                    Intent intent = new Intent(getActivity(), PersonsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Du musst noch eine Person eingeben.",
                            Toast.LENGTH_SHORT).show();
                }
                //todo: neu
                loadEmptyAddView();
            }else if(personsAddFragmentStatus == STATUS_UPDATE){
                updateEntry();
            }
        } else if(v.getId()==R.id.button_calendarCall) {
            //todo: Code zitieren: https://stackoverflow.com/questions/1943679/android-calendar (abgerufen am 27.08.2019)

            Intent i = new Intent();

            ComponentName cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");

            i.setComponent(cn);
            startActivity(i);
        }
    }

    private void createNotification(String title, String text) {
        createNotificationChannel();
        Notification.Builder mBuilder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            mBuilder =
                    new Notification.Builder(getActivity(), NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.app_name)).setContentText(title)
                            .setStyle(new Notification.BigTextStyle().bigText(text)).setAutoCancel(true);
        }
        else {
            mBuilder =
                    new Notification.Builder(getActivity()).setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(getString(R.string.app_name)).setContentText(title)
                            .setStyle(new Notification.BigTextStyle().bigText(text)).setAutoCancel(true);
        }

        // Check if we're running on Android 5.0 or higher, older versions don't support visibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            mBuilder.setVisibility(Notification.VISIBILITY_SECRET);
        }
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getActivity(), PersonsActivity.class);
        resultIntent
                .putExtra(INTENT_ITEM_SELECTED_NAME, INTENT_ITEM_SELECTED_ID);
        resultIntent.putExtra(INTENT_ITEM_SELECTED_NAME, INTENT_ITEM_SELECTED_ID);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of your application to
        // the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
        // Adds the back stack for the Intent (but not the Intent itself)

        //todo: PersonsAddActivity für Smartphone
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }


    //todo: neu L
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_LOW;
            CharSequence channelName = NOTIFICATION_CHANNEL_NAME;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void saveEntry(View v) {
        // todo: kann man hier nachad eigtl rauslöschen? (oben schon alle)
        //findViewsById();
        // getInformation(v);

        //todo: Neu repository oben (hier Erzeugung rauslöschen

        // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Erstellung Repository mit richtigem Kontext
        // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Kontext der Activity des Fragments: Präfix: getActivity()
        //repository = new Repository(getActivity().getApplicationContext());

        repository.insertPersonEvent(textFirstName,textSurName, eventType, eventDateInt);
    }

    //todo: NEU
    private void updateEntry(){
        repository.updatePersonEvent(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, eventDateToUpdateInt, textFirstName,textSurName, eventType, eventDateInt);
    }

    //todo: NEU
    protected void loadEmptyAddView(){
        editText_firstName.setText("");
        editText_surName.setText("");
        editText_eventDate.setText("");
        editText_eventType.setText("");

        //todo: NEU rauslöschen
        //https://stackoverflow.com/questions/39451359/android-spinner-set-selected-item-as-default/39451650
        //initiale Selection setten
        //spinner_eventType.setSelection(EVENTTYPE_BIRTHDAY);
    }


    //todo: NEU (Übergabe view + Verwendung view (getView funktioniert bei mir irgendwie nicht)
    private void findViewsById(View view) {
        editText_firstName = view.findViewById(R.id.editText_firstName2);
        editText_surName = view.findViewById(R.id.editText_surName2);
        editText_eventDate = view.findViewById(R.id.editText_eventDate);
        editText_eventType = view.findViewById(R.id.editText_eventType);
        //spinner_eventType = getView().findViewById(R.id.spinner_eventType);

        //todo: Neu (alles auf einem Haufen)
        button_done = view.findViewById(R.id.button_done2);
        button_calendarCall = view.findViewById(R.id.button_calendarCall);
    }

    //todo: Neu
    private void getInformation() {
        textFirstName = editText_firstName.getText().toString();
        textSurName = editText_surName.getText().toString();

        eventDate = editText_eventDate.getText().toString();

        /**Pankaj. (n.d.).
         * Java Remove Character from String.
         * Retrieved from: https://www.journaldev.com/18361/java-remove-character-string.
         * characters ersetzen + String kürzen*/
        eventDateInt = Integer.parseInt(eventDate.replace(".", "").substring(0, 4));

        //int eventTypeInt = spinner_eventType.getSelectedItemPosition();
        //eventType = getEvent(eventTypeInt);
        eventType = editText_eventType.getText().toString();
    }

    //todo: Neu (test)
    protected void setInformation(){
        editText_firstName.setText(personFirstNameToUpdate);
        editText_surName.setText(personLastNameToUpdate);
        editText_eventType.setText(eventNameToUpdate);
        editText_eventDate.setText(eventDateToUpdateString);
    }

    // todo: Neu
    protected void onPersonsUpdate(String personFirstName, String personLastName, String eventName, String eventDate) {
        personFirstNameToUpdate = personFirstName;
        personLastNameToUpdate = personLastName;
        eventNameToUpdate = eventName;
        eventDateToUpdateString = eventDate;

        String eventDateToParse = eventDateToUpdateString;
        /**Pankaj. (n.d.).
         * Java Remove Character from String.
         * Retrieved from: https://www.journaldev.com/18361/java-remove-character-string.
         * characters ersetzen*/
        eventDateToParse = eventDateToParse.replace(".", "");
        if(eventDateToParse.equals("")){
            eventDateToParse = "0";
        }

        eventDateToUpdateInt = Integer.parseInt(eventDateToParse);
    }

    //todo: Neu
    protected void setStatus(int status){
        //Unterscheidung, ob Geschenk im Hinzufügen-/Update-Modus ist
        personsAddFragmentStatus = status;
    }

    //todo: neu
    @Override
    public void onPostAddPerson() {
        Toast.makeText(getActivity(), "Eintrag wurde hinzugefügt",
                Toast.LENGTH_SHORT).show();
    }

    //todo: neu
    @Override
    public void onPostUpdatePerson() {
        Toast.makeText(getActivity(), "Eintrag wurde aktualisiert",
                Toast.LENGTH_SHORT).show();
    }
}
