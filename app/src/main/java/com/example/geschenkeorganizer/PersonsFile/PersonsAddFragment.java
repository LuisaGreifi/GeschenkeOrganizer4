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

    private String eventType, eventDate;
    private int eventDateInt;

    private EditText editText_firstName, editText_surName, editText_eventDate, editText_eventType;
    private Button button_done, button_calendarCall;

    private String textFirstName, textSurName;

    private Repository repository;
    //Konstante, die gesetzt wird, wenn Klick auf ListItem stattfindet und PersonsAddFragment deswegen angepasst werden soll
    //initial: add
    private int personsAddFragmentStatus = STATUS_ADD;
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;

    //Inhalte, die in Item gespeichert sind --> sollen angezeigt/updatebar sein
    private String personFirstNameToUpdate,personLastNameToUpdate, eventNameToUpdate, eventDateToUpdateString;
    private int eventDateToUpdateInt;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_done2) {

            getInformation();

            if (textFirstName.isEmpty() || textSurName.isEmpty() || eventDate.isEmpty() || eventType.isEmpty()) {
                Toast.makeText(getActivity(), "Bitte fülle alle Felder aus",
                        Toast.LENGTH_SHORT).show();
            } else {
                //Unterscheidung, ob Geschenk hinzugefügt oder geupdatet wird
                if (personsAddFragmentStatus == STATUS_ADD) {
                    createNotification("Geschenke-Erinnerung", "Überlege dir ein Geschenk ;-)");
                    saveEntry(v);
                    Intent intent = new Intent(getActivity(), PresentsActivity.class);
                    startActivity(intent);
                    loadEmptyAddView();
                 }else if (personsAddFragmentStatus == STATUS_UPDATE) {
                    updateEntry();
            }
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
        if(!eventDate.isEmpty()){
            eventDateInt = Integer.parseInt(eventDate.replace(".", "").substring(0, 4));
        }

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
        Toast.makeText(getActivity(), "Eintrag wurde hinzugefügt",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostUpdatePerson() {
        Toast.makeText(getActivity(), "Eintrag wurde aktualisiert",
                Toast.LENGTH_SHORT).show();
    }
}
