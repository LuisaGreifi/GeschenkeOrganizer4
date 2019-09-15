package com.example.geschenkeorganizer.presents;

import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.Repository;

public class PresentsAddFragment extends Fragment implements View.OnClickListener {

    //todo: NEU (ausgeklammert: Interface)
    /**
    private OnListItemChangedListener mCallback;

    public interface OnListItemChangedListener {
        public void onListItemChanged();
    }
     */

    private EditText firstName, surName, description, event, placeOfPurchase, price;
    private CheckBox hadIdea, bought, wrapped;
    private Button done;

    private String textFirstName, textSurName, textDescription, textPlaceOfPurchase, StringTextPrice, /**todo:NEU */textStatus;
    private boolean booHadIdea, booBought, booWrapped;
    private double textPrice;

    private String eventType;
    private static final int EVENTTYPE_BIRTHDAY = 0, EVENTTYPE_CHRISTMAS = 1, EVENTTYPE_ANNIVERSARY = 2, EVENTTYPE_WEDDING = 3, EVENTTYPE_VALENTINESDAY = 4, EVENTTYPE_MOTHERSDAY = 5, EVENTTYPE_FATHERSDAY = 6, EVENTTYPE_NAMEDAY = 7, EVENTTYPE_OTHER = 8;

    private Repository repository;

    //Konstante, die gesetzt wird, wenn Klick auf ListItem stattfindet und PresentsAddFragment deswegen angepasst werden soll
    //initial: add
    private int presentsAddFragmentStatus = STATUS_ADD;
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;

    //Inhalte, die in Item gespeichert sind --> sollen angezeigt/updatebar sein
    private String presentNameToUpdate, personFirstNameToUpdate,personLastNameToUpdate, eventNameToUpdate, priceToUpdateString, shopToUpdate, statusToUpdate;
    private double priceToUpdateDouble;

    //Konstanten für Benachrichtigungen
    private final static int NOTIFICATION_ID = 0;
    private final static String NOTIFICATION_CHANNEL_NAME = "CH0";
    private final static String NOTIFICATION_CHANNEL_ID = "0";


    public PresentsAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presents_add, container, false);

        findViewsById(view);
        //TextViews nur setten, wenn davor listItem angeklickt wurde
        if(presentsAddFragmentStatus == STATUS_UPDATE){
            setInformation();
        }

        done.setOnClickListener(this);

        /**Murthy, A. (04.05.2018).
         * 5 steps to implement Room persistence library in Android.
         * Retrieved from https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24.
         * Erstellung Repository mit richtigem Kontext;
         * Kontext der Activity des Fragments: Präfix: getActivity() */
        repository = new Repository(getActivity().getApplicationContext());

        return view;
    }

    @Override
    public void onClick(View v) {
        getInformation();

        //Unterscheidung, on Geschenk hinzugefügt oder geupdatet wird
        if(presentsAddFragmentStatus == STATUS_ADD){
            if (!firstName.getText().toString().isEmpty() && !surName.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
                if(!(bought.isChecked()) && !(wrapped.isChecked())) {
                    createNotification("Kaufe und verpacke dein Geschenk ;-)", "Geschenke-Erinnerung");
                }
                else if(!(bought.isChecked())) {
                    createNotification("verpackt aber nicht gekauft...interessant! ;-)", "Geschenke-Erinnerung");
                }
                else if(!(wrapped.isChecked())) {
                    createNotification("Verpacke dein Geschenk noch ;-)", "Geschenke-Erinnerung");
                }

                saveEntry();
                // todo: für Insert erstmal nicht relevant
                //mCallback.onListItemChanged();
            } else {
                Toast.makeText(getActivity(), "Du musst noch eine Person und/oder die Beschreibung des Geschenks eingeben.",
                        Toast.LENGTH_SHORT).show();
            }
            loadEmptyAddView();
        }else if(presentsAddFragmentStatus == STATUS_UPDATE){
            updateEntry();
        }
    }

    //Retrieved from NoteTaker-Übung
    //todo: Zitat anpassen
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

        // wenn Android 5.0 oder höher vorhanden ist, dann visibility setzen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_SECRET);
        }

        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
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

    private void saveEntry() {
        repository.insertPresent(textFirstName,textSurName, eventType, textDescription, textPrice, textPlaceOfPurchase, textStatus);
    }

    private void updateEntry(){
        repository.updatePresent(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, presentNameToUpdate, priceToUpdateDouble, shopToUpdate, statusToUpdate, textFirstName,textSurName, eventType, textDescription, textPrice, textPlaceOfPurchase, textStatus);
    }

    protected void loadEmptyAddView() {
        firstName.setText("");
        surName.setText("");
        description.setText("");
        event.setText("");
        placeOfPurchase.setText("");
        price.setText("");

        /**Android Developers. (n.d.).
         * CheckBox.
         * Retrieved from: https://developer.android.com/reference/android/widget/CheckBox.
         * Checkboxes auf leer setzen*/
        hadIdea.setChecked(false);
        bought.setChecked(false);
        wrapped.setChecked(false);
    }

    private void findViewsById(View view) {
        firstName = view.findViewById(R.id.editText_firstName);
        surName = view.findViewById(R.id.editText_surName);
        description = view.findViewById(R.id.editText_description);
        event = view.findViewById(R.id.editText_event);
        placeOfPurchase = view.findViewById(R.id.editText_placeOfPurchase);
        price = view.findViewById(R.id.editText_price);
        hadIdea = view.findViewById(R.id.checkBox_hadIdea);
        bought = view.findViewById(R.id.checkBox_bought);
        wrapped = view.findViewById(R.id.checkBox_wrapped);
        done = view.findViewById(R.id.button_done);
    }

    private void getInformation() {
        textFirstName = firstName.getText().toString();
        textSurName = surName.getText().toString();
        textDescription = description.getText().toString();
        textPlaceOfPurchase = placeOfPurchase.getText().toString();
        StringTextPrice = price.getText().toString();
        String priceToParse = StringTextPrice;

        /**Pankaj. (n.d.).
         * Java Remove Character from String.
         * Retrieved from: https://www.journaldev.com/18361/java-remove-character-string.
         * characters ersetzen*/
        priceToParse = priceToParse.replace(",", ".");
        priceToParse = priceToParse.replace(" ", "");
        priceToParse = priceToParse.replace("€", "");
        priceToParse = priceToParse.replace(" €", "");

        textPrice = Double.parseDouble(priceToParse);


        //todo: EditText im Layout über Attribute näher definieren (nur Komma-/Punktzahlen eingeben)
        //todo: unteren textPrice rauslöschen!

        eventType = event.getText().toString();

        //todo: Stati noch ändern (Idee eigtl unnötig!)
        booHadIdea = hadIdea.isChecked();
        booBought = bought.isChecked();
        booWrapped = wrapped.isChecked();

        textStatus = getHighestStatusOfCheckboxes();
    }

    //todo: Stati noch ändern (Idee eigtl unnötig!)
    private String getHighestStatusOfCheckboxes(){
        String result = "";
        String hadIdea = "Idee";
        String bought = "gekauft";
        String wrapped = "verpackt";
        if((booHadIdea && booBought && booWrapped) || (booHadIdea && booWrapped) || (booBought && booWrapped) || booWrapped ){
            result = wrapped;
        } else if ((booHadIdea && booBought) || booBought){
            result = bought;
        } else if (booHadIdea){
            result = hadIdea;
        }
        return result;
    }

    protected void setInformation(){
        firstName.setText(personFirstNameToUpdate);
        surName.setText(personLastNameToUpdate);
        description.setText(presentNameToUpdate);
        event.setText(eventNameToUpdate);
        placeOfPurchase.setText(shopToUpdate);
        price.setText(priceToUpdateString);

        /**Android Developers. (n.d.).
         * CheckBox.
         * Retrieved from: https://developer.android.com/reference/android/widget/CheckBox.
         * Checkboxes überprüfen und gegebenenfalls setzen*/
        if(statusToUpdate.equals("Idee")){
            hadIdea.setChecked(true);
        } else if(statusToUpdate.equals("gekauft")){
            bought.setChecked(true);
        } else if(statusToUpdate.equals("verpackt")){
            wrapped.setChecked(true);
        }
    }


    protected void onPresentUpdate(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status) {
        presentNameToUpdate = presentName;
        personFirstNameToUpdate = personFirstName;
        personLastNameToUpdate = personLastName;
        eventNameToUpdate = eventName;
        priceToUpdateString = price;
        /**Pankaj. (n.d.).
         * Java Remove Character from String.
         * Retrieved from: https://www.journaldev.com/18361/java-remove-character-string.
         * characters ersetzen*/
        priceToUpdateDouble = Double.parseDouble(price.replace(" €", ""));

        shopToUpdate = shop;
        statusToUpdate = status;

    }

    protected void setStatus(int status){
        //Unterscheidung, ob Geschenk im Hinzufügen-/Update-Modus ist
        presentsAddFragmentStatus = status;
    }
}