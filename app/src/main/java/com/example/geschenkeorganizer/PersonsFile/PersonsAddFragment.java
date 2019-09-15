package com.example.geschenkeorganizer.PersonsFile;

import androidx.fragment.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geschenkeorganizer.MainActivity;
import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.access.Repository;
import com.example.geschenkeorganizer.presents.PresentsActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PersonsAddFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, PersonsAddListener{

    private String textFirstName, textSurName, eventType, eventDate;
    private int eventDateInt;

    private EditText editText_firstName, editText_surName, editText_eventDate, editText_eventType;
    private Button button_done, button_calendarCall;

    private Repository repository;

    //Konstante, die gesetzt wird, wenn Klick auf ListItem stattfindet und PersonsAddFragment deswegen angepasst werden soll
    //initial: add
    private int personsAddFragmentStatus = STATUS_ADD;
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;

    //Inhalte, die in Item gespeichert sind --> sollen angezeigt/updatebar sein
    private String personFirstNameToUpdate,personLastNameToUpdate, eventNameToUpdate, eventDateToUpdateString;
    private int eventDateToUpdateInt;

    //Konstanten für Benachrichtigungen
    private final static int NOTIFICATION_ID = 0;
    private final static String NOTIFICATION_CHANNEL_NAME = "CH0";
    private final static String NOTIFICATION_CHANNEL_ID = "0";
    public static final int INTENT_ITEM_SELECTED_ID = 0;
    public static final String INTENT_ITEM_SELECTED_NAME = "IntentForLoading";


    public PersonsAddFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persons_add, container, false);

        findViewsById(view);

        //TextViews nur setten, wenn davor listItem angeklickt wurde
        if(personsAddFragmentStatus == STATUS_UPDATE){
            setInformation();
        }

        button_done.setOnClickListener(this);
        button_calendarCall.setOnClickListener(this);

        initEventDate();

        /**Murthy, A. (04.05.2018).
         * 5 steps to implement Room persistence library in Android.
         * Retrieved from https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24.
         * Erstellung Repository mit richtigem Kontext;
         * Kontext der Activity des Fragments: Präfix: getActivity() */
        repository = new Repository(getActivity().getApplicationContext());

        repository.setPersonsAddListener(this);

        return view;
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

    //Kalender auf aktuelles Datum setzen
    private DatePickerDialog createDatePickerDialog() {
        GregorianCalendar today = new GregorianCalendar();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    //Datum formatieren und in EditText einfügen
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        eventDate = df.format(date.getTime());
        editText_eventDate.setText(eventDate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_done2) {
            getAndChangeInformation(v);

        } else if(v.getId()==R.id.button_calendarCall) {
           callCalendar();
        }
    }

    private void getAndChangeInformation(View v) {
        getInformation();

        //Unterscheidung, ob Geschenk hinzugefügt oder geupdatet wird
        if (personsAddFragmentStatus == STATUS_ADD){
            if (!editText_firstName.getText().toString().isEmpty() && !editText_surName.getText().toString().isEmpty() && !editText_eventType.getText().toString().isEmpty() && !editText_eventType.getText().toString().isEmpty() && !editText_eventDate.getText().toString().isEmpty()) {
                createNotification(getResources().getString(R.string.notification_title), getResources().getString(R.string.notification_text_thinkAboutPresent));
                saveEntry(v);
                Intent intent = new Intent(getActivity(), PresentsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_fillOutAllFields),
                        Toast.LENGTH_SHORT).show();
            }
            loadEmptyAddView();
        } else if(personsAddFragmentStatus == STATUS_UPDATE){
            updateEntry();
        }
    }

    private void callCalendar() {
        /** djk (24.03.2011)
         * android calendar [Online forum comment]
         * Retrieved from https://stackoverflow.com/questions/1943679/android-calendar
         * Kalender wird über einen Intent aufgerufen*/
        Intent i = new Intent();
        ComponentName cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
        i.setComponent(cn);
        startActivity(i);
    }

    /**vgl. Schwappach F. & Jelinski J. (25.05.2019).
     * Übungsaufgabe 11. NoteTaker [Lösung zur Übung].
     * Retrieved from https://ilias.uni-passau.de/ilias/goto.php?target=root_1&client_id=intelec
     * Benachrichtigung wird mithilfe des Notification.Builder, TastStackBuilder und Intents erstellt*/
    private void createNotification(String title, String text) {
        createNotificationChannel();
        Notification.Builder mBuilder;
        mBuilder = differentiateBuildVersions(title, text);

        // wenn Android 5.0 oder höher vorhanden ist, dann visibility setzen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
            mBuilder.setVisibility(Notification.VISIBILITY_SECRET);
        }

        Intent resultIntent = createExplicitIntent();
        TaskStackBuilder stackBuilder = createStackBuilder(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = createAndNotifyNotificationManager(mBuilder);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private Notification.Builder differentiateBuildVersions(String title, String text) {
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
        return  mBuilder;
    }

    private Intent createExplicitIntent() {
        // erstellt expliziten Intent
        Intent resultIntent = new Intent(getActivity(), PersonsActivity.class);
        resultIntent
                .putExtra(INTENT_ITEM_SELECTED_NAME, INTENT_ITEM_SELECTED_ID);
        resultIntent.putExtra(INTENT_ITEM_SELECTED_NAME, INTENT_ITEM_SELECTED_ID);
        return resultIntent;
    }

    private TaskStackBuilder createStackBuilder(Intent resultIntent) {
        // erstellt einen künstlichen Back Stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());

        // fügt den Back Stack für den Intent hinzu (nicht Intent selbst)
        stackBuilder.addParentStack(MainActivity.class);

        // fügt den Intent hinzu, der die Activity oben auf den Stack legt
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder;
    }

    private NotificationManager createAndNotifyNotificationManager(Notification.Builder mBuilder) {
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

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
        repository.insertPersonEvent(textFirstName,textSurName, eventType, eventDateInt);
    }

    private void updateEntry(){
        repository.updatePersonEvent(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, eventDateToUpdateInt, textFirstName,textSurName, eventType, eventDateInt);
    }

    protected void loadEmptyAddView(){
        editText_firstName.setText("");
        editText_surName.setText("");
        editText_eventDate.setText("");
        editText_eventType.setText("");
    }

    private void findViewsById(View view) {
        editText_firstName = view.findViewById(R.id.editText_firstName2);
        editText_surName = view.findViewById(R.id.editText_surName2);
        editText_eventDate = view.findViewById(R.id.editText_eventDate);
        editText_eventType = view.findViewById(R.id.editText_eventType);
        button_done = view.findViewById(R.id.button_done2);
        button_calendarCall = view.findViewById(R.id.button_calendarCall);
    }

    private void getInformation() {
        textFirstName = editText_firstName.getText().toString();
        textSurName = editText_surName.getText().toString();

        eventDate = editText_eventDate.getText().toString();

        /**Pankaj. (n.d.).
         * Java Remove Character from String.
         * Retrieved from: https://www.journaldev.com/18361/java-remove-character-string.
         * characters ersetzen + String kürzen*/
        eventDateInt = Integer.parseInt(eventDate.replace(".", "").substring(0, 4));

        eventType = editText_eventType.getText().toString();
    }

    protected void setInformation(){
        editText_firstName.setText(personFirstNameToUpdate);
        editText_surName.setText(personLastNameToUpdate);
        editText_eventType.setText(eventNameToUpdate);
        editText_eventDate.setText(eventDateToUpdateString);
    }

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

    protected void setStatus(int status){
        //Unterscheidung, ob Geschenk im Hinzufügen-/Update-Modus ist
        personsAddFragmentStatus = status;
    }

    @Override
    public void onPostAddPerson() {
        Toast.makeText(getActivity(), getString(R.string.toast_addedEntry),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostUpdatePerson() {
        Toast.makeText(getActivity(), getString(R.string.toast_addedEntry),
                Toast.LENGTH_SHORT).show();
    }
}
