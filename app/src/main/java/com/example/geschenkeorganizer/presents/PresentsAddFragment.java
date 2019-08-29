package com.example.geschenkeorganizer.presents;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.Repository;

public class PresentsAddFragment extends Fragment implements View.OnClickListener {

    private OnListItemChangedListener mCallback;

    public interface OnListItemChangedListener {
        public void onListItemChanged();
    }

    private EditText firstName, surName, description, event, placeOfPurchase, price;
    private CheckBox hadIdea, bought, wrapped;
    private Button done;

    private String textFirstName, textSurName, textDescription, textEvent, textPlaceOfPurchase, StringTextPrice, /**todo:NEU */textStatus;;
    private boolean booHadIdea, booBought, booWrapped;
    private double textPrice;

    private Repository repository;


    public PresentsAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presents_add, container, false);
        Button doneButton = view.findViewById(R.id.button_done);
        doneButton.setOnClickListener(this);
        return view;
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
        //todo: NEU (Kein Plan, ob das hier richtig initialisiert ist + ob dann wirklich bloß 1 Db pro Activity erzeugt wird)

        // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Erstellung Repository mit richtigem Kontext
        // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
        // Kontext der Activity des Fragments: Präfix: getActivity()
        repository = new Repository(getActivity().getApplicationContext());

        //todo: NEU
        repository.insertPresent(textFirstName,textSurName, textEvent, textDescription, textPrice, textPlaceOfPurchase, textStatus);

        // todo: am Besten Einträge rauslöschen --> Nutzer, sieht, das gespeicehrt wurde; am besten in Post-Execute (Nicht, das Daten gelöscht werden, bevor sie gespeichert wurden)
    }

    public void loadEmptyAddView() {
        //todo: alle Views ohne Text etc. laden
    }

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
        textEvent = event.getText().toString();
        textPlaceOfPurchase = placeOfPurchase.getText().toString();
        StringTextPrice = price.getText().toString();

        //todo:neu (textPrice unten is mejor!), funktioniert soweit!
        //todo:obacht: es dürfen nur "."(Punkt) doubles eingegeben werden!

        // https://www.journaldev.com/18361/java-remove-character-string
        // characters ersetzen
        textPrice = Double.parseDouble(StringTextPrice.replace(",", "."));


        //todo: EditText im Layout über Attribute näher definieren (nur Komma-/Punktzahlen eingeben)
        textPrice = Double.valueOf(StringTextPrice);
        //todo: nachschauen, ob isChecked richtige Methode ist
        booHadIdea = hadIdea.isChecked();
        booBought = bought.isChecked();
        booWrapped = wrapped.isChecked();

        //todo: neu
        textStatus = getHighestStatusOfCheckboxes();
    }

    //todo: neu
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