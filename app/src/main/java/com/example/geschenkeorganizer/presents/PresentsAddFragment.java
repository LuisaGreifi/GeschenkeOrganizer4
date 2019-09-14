package com.example.geschenkeorganizer.presents;

//todo: NEU
//import androidx Fragment!
import androidx.fragment.app.Fragment;
//import android.app.Fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.geschenkeorganizer.database.PresentListClickListener;
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

    //todo: neu
    private Spinner spinner_event;
    private String eventType;
    private static final int EVENTTYPE_BIRTHDAY = 0, EVENTTYPE_CHRISTMAS = 1, EVENTTYPE_ANNIVERSARY = 2, EVENTTYPE_WEDDING = 3, EVENTTYPE_VALENTINESDAY = 4, EVENTTYPE_MOTHERSDAY = 5, EVENTTYPE_FATHERSDAY = 6, EVENTTYPE_NAMEDAY = 7, EVENTTYPE_OTHER = 8;

    private Repository repository;

    //todo: Neu
    //Konstante, die gesetzt wird, wenn Klick auf ListItem stattfindet und PresentsAddFragment deswegen angepasst werden soll
    //initial: add
    private int presentsAddFragmentStatus = STATUS_ADD;
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;

    // todo: Neu
    //Inhalte, die in Item gespeichert sind --> sollen angezeigt/updatebar sein
    private String presentNameToUpdate, personFirstNameToUpdate,personLastNameToUpdate, eventNameToUpdate, priceToUpdateString, shopToUpdate, statusToUpdate;
    private double priceToUpdateDouble;


    public PresentsAddFragment() {
    }

    //todo: in PersonsAdFragment wird hier Spinner initialisiert?
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presents_add, container, false);

        //todo: NEU
        //hier Views finden --> nicht später überall einzeln + View übergeben (getView klappt bei mir irgendwie nicht mehr?)
        findViewsById(view);
        //TextViews nur setten, wenn davor listItem angeklickt wurde
        if(presentsAddFragmentStatus == STATUS_UPDATE){
            setInformation();
        }


        //todo: Neu (Initialisierung Button in findViewById --> kann rausgelöscht werden
        //todo: Neu doneButton --> done (oben initialisiert)
        // Button doneButton = view.findViewById(R.id.button_done);
        done.setOnClickListener(this);

        //todo: neu
        // davor in saveEntry (wird aber ja dann jedes Mal erzeugt --> madig)
        //https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Erstellung Repository mit richtigem Kontext
        // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Kontext der Activity des Fragments: Präfix: getActivity()
        repository = new Repository(getActivity().getApplicationContext());

        return view;
    }


    //todo: müsste oben aufgerufen werden + Spinner spinenr übergeben werden
    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),  R.array.eventTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_event.setAdapter(adapter);

        spinner_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v,
                                       int position, long arg3) {
                int eventTypeInt = spinner_event.getSelectedItemPosition();
                eventType = getEvent(eventTypeInt);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private String getEvent(int eventTypeInt) {
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


    @Override
    public void onClick(View v) {
        //todo: Neu: denk das kann man rauslöschen...oben in findViewsById
        /**
        firstName = getView().findViewById(R.id.editText_firstName);
        surName = getView().findViewById(R.id.editText_surName);
        description = getView().findViewById(R.id.editText_description);
         */

        //todo:NEU
        //das hier, nicht in save + update extra
        getInformation();

        //todo: NEU
        //Unterscheidung, on Geschenk hinzugefügt oder geupdatet wird
        if(presentsAddFragmentStatus == STATUS_ADD){
            if (!firstName.getText().toString().isEmpty() && !surName.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
                saveEntry();
                // todo: für Insert erstmal nicht relevant
                //mCallback.onListItemChanged();
            } else {
                Toast.makeText(getActivity(), "Du musst noch eine Person und/oder die Beschreibung des Geschenks eingeben.",
                        Toast.LENGTH_SHORT).show();
            }
            //todo: neu
            loadEmptyAddView();
        }else if(presentsAddFragmentStatus == STATUS_UPDATE){
            updateEntry();
        }
    }

    //todo: NEU (Parameter View v braucht man nicht --> rauslöschen) + getInformation schon in onCLick, nicht in saveEntry
    private void saveEntry() {
        // todo: kann man hier nachad eigtl rauslöschen? (oben schon alle)
        // findViewsById();
        // getInformation();
        repository.insertPresent(textFirstName,textSurName, eventType, textDescription, textPrice, textPlaceOfPurchase, textStatus);
    }

    //todo: NEU
    private void updateEntry(){
        //todo: get Present By id --> erst später :)
        repository.updatePresent(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, presentNameToUpdate, priceToUpdateDouble, shopToUpdate, statusToUpdate, textFirstName,textSurName, eventType, textDescription, textPrice, textPlaceOfPurchase, textStatus);
    }




    //todo: neu
    protected void loadEmptyAddView() {
        firstName.setText("");
        surName.setText("");
        description.setText("");
        event.setText("");
        placeOfPurchase.setText("");
        price.setText("");

        //https://developer.android.com/reference/android/widget/CheckBox
        //Checkbox checken (setten bzw ent-setten)
        hadIdea.setChecked(false);
        bought.setChecked(false);
        wrapped.setChecked(false);
    }

    //todo: NEU (Übergabe view + Verwendung view (getView funktioniert bei mir irgendwie nicht)
    //todo: spinner heißt hier anders bzw. existiert noch gar nicht in Layout!! KEIN EditText
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

    //todo: NEU (Parameter View v braucht man nicht --> rauslöschen
    private void getInformation() {
        textFirstName = firstName.getText().toString();
        textSurName = surName.getText().toString();
        textDescription = description.getText().toString();
        textPlaceOfPurchase = placeOfPurchase.getText().toString();
        StringTextPrice = price.getText().toString();

        //todo: NEU
        //todo: evntl auch andere Sachen replacen... €, Leerzeichen zu NICHTS
        //todo: VORSICHT, sehr fehleranfällig...

        // https://www.journaldev.com/18361/java-remove-character-string
        // characters ersetzen
        String priceToParse = StringTextPrice;
        priceToParse = priceToParse.replace(",", ".");
        priceToParse = priceToParse.replace(" ", "");
        priceToParse = priceToParse.replace("€", "");
        priceToParse = priceToParse.replace(" €", "");
        Log.d("PresentsAddFragment", priceToParse);
        textPrice = Double.parseDouble(priceToParse);


        //todo: EditText im Layout über Attribute näher definieren (nur Komma-/Punktzahlen eingeben)
        //todo: unteren textPrice rauslöschen!

        // textPrice = Double.valueOf(StringTextPrice);

        //todo: neu Spinner haut so noch nicht hin! (vorübergehend auskommentiert!)
        /** int eventTypeInt = spinner_event.getSelectedItemPosition();
         eventType = getEvent(eventTypeInt);
         */

        //todo: neu vorübergehend noch/wieder event (später rauslöschen)
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

    //todo: Neu (test)
    //todo: hier evntl auch Button anpassen (setInformation)
    protected void setInformation(){
        firstName.setText(personFirstNameToUpdate);
        surName.setText(personLastNameToUpdate);
        description.setText(presentNameToUpdate);
        event.setText(eventNameToUpdate);
        placeOfPurchase.setText(shopToUpdate);
        price.setText(priceToUpdateString);

        //https://developer.android.com/reference/android/widget/CheckBox
        //Checkbox checken (setten)
        //todo: schöner: switch-case
        //todo: schöner Konstanten (wird obe nzum speichern auch benötigt
        if(statusToUpdate.equals("Idee")){
            hadIdea.setChecked(true);
        } else if(statusToUpdate.equals("gekauft")){
            bought.setChecked(true);
        } else if(statusToUpdate.equals("verpackt")){
            wrapped.setChecked(true);
        }
    }


    // todo: Neu
    protected void onPresentUpdate(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status) {
        Log.d("PresentsAddFragment", "onUpdate");
        presentNameToUpdate = presentName;
        personFirstNameToUpdate = personFirstName;
        personLastNameToUpdate = personLastName;
        eventNameToUpdate = eventName;
        priceToUpdateString = price;
        // https://www.journaldev.com/18361/java-remove-character-string
        // characters ersetzen
        priceToUpdateDouble = Double.parseDouble(price.replace(" €", ""));

        shopToUpdate = shop;
        statusToUpdate = status;

    }

    //todo: Neu
    protected void setStatus(int status){
        //Konstante --> Unterscheidung, ob Geschenk hinzugefügt/geupdatet wird
        presentsAddFragmentStatus = status;
    }
}