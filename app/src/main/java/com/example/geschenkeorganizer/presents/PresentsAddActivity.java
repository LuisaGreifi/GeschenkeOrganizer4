package com.example.geschenkeorganizer.presents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.R;

//todo: Neu Interface auskommentiert
public class PresentsAddActivity extends AppCompatActivity /**implements PresentsAddFragment.OnListItemChangedListener */{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.add_presents);
        Intent intent = getIntent();
        //todo: Neu (Test)
        //VL 3
        //Intent auslesen
        Bundle extras = intent.getExtras();

       //https://stackoverflow.com/questions/13408419/how-do-i-tell-if-intent-extras-exist-in-android/40524418
        //extras nur getten, wenn sie existieren
        // = Unterscheidung, von welchem Intent Activity gestartet wurde
        if(extras != null){
            Log.d("PresentsAddActivity", "Extras erfasst");
            //VL 3
            //Intent auslesen
            String presentName = extras.getString("presentNameString");
            String personFirstName = extras.getString("presentFirstNameString");
            String personLastName = extras.getString("personLastNameString");
            String eventName = extras.getString("eventNameString");
            String price = extras.getString("priceString");
            String shop = extras.getString("shopString");
            String status = extras.getString("statusString");

            //https://developer.android.com/training/basics/fragments/communicating
            // direkter Aufruf der Fragment Methode möglich
            PresentsAddFragment paf =
                    (PresentsAddFragment) getFragmentManager().findFragmentById(R.layout.fragment_presents_add);
            if (paf != null) {
                Log.d("PresentsAddActivity", "Paf !=0");
                paf.onUpdatePresentItem(presentName, personFirstName, personLastName, eventName, price, shop, status);
            }
        }



        if (intent == null) {
            Toast.makeText(this, "Erstelle das neue Geschenk auf der rechten Seite.", Toast.LENGTH_SHORT).show();
        }
    }



    //todo: Neu Interface-Methode auskommentiert
    /**
    @Override
    public void onListItemChanged() {
        //todo: Datenbank Bescheid geben?
    }
    */
}