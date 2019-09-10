package com.example.geschenkeorganizer.presents;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.PresentViewModel;
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


    public PresentsAddFragment() {
    }

    //todo: in PersonsAdFragment wird hier Spinner initialisiert?
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presents_add, container, false);
        Button doneButton = view.findViewById(R.id.button_done);
        doneButton.setOnClickListener(this);

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
        firstName = getView().findViewById(R.id.editText_firstName);
        surName = getView().findViewById(R.id.editText_surName);
        description = getView().findViewById(R.id.editText_description);
        if (!firstName.getText().toString().isEmpty() && !surName.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
            saveEntry(v);
            // todo: für Insert erstmal nicht relevant
            //mCallback.onListItemChanged();

        } else {
            Toast.makeText(getActivity(), "Du musst noch eine Person und/oder die Beschreibung des Geschenks eingeben.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void saveEntry(View v) {
        findViewsById();
        getInformation(v);

        repository.insertPresent(textFirstName,textSurName, eventType, textDescription, textPrice, textPlaceOfPurchase, textStatus);

        // todo: am Besten Einträge rauslöschen --> Nutzer, sieht, das gespeicehrt wurde; am besten in Post-Execute (Nicht, das Daten gelöscht werden, bevor sie gespeichert wurden)
    }

    public void loadEmptyAddView() {
        //todo: alle Views ohne Text etc. laden
    }

    //todo: spinner heißt hier anders bzw. existiert noch gar nicht in Layout!! KEIN EditText
    private void findViewsById() {
        firstName = getView().findViewById(R.id.editText_firstName);
        surName = getView().findViewById(R.id.editText_surName);
        description = getView().findViewById(R.id.editText_description);
        event = getView().findViewById(R.id.editText_event);
        placeOfPurchase = getView().findViewById(R.id.editText_placeOfPurchase);
        price = getView().findViewById(R.id.editText_price);
        hadIdea = getView().findViewById(R.id.checkBox_hadIdea);
        bought = getView().findViewById(R.id.checkBox_bought);
        wrapped = getView().findViewById(R.id.checkBox_wrapped);
        done = getView().findViewById(R.id.button_done);
    }

    private void getInformation(View v) {
        textFirstName = firstName.getText().toString();
        textSurName = surName.getText().toString();
        textDescription = description.getText().toString();
        textPlaceOfPurchase = placeOfPurchase.getText().toString();
        StringTextPrice = price.getText().toString();


        //todo:obacht: es dürfen nur "."(Punkt) doubles eingegeben werden!
        //todo: muss noch geändert werden --> Fehlermeldung

        // https://www.journaldev.com/18361/java-remove-character-string
        // characters ersetzen
        textPrice = Double.parseDouble(StringTextPrice.replace(",", "."));


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
}