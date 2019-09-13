package com.example.geschenkeorganizer.presents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.PresentListClickListener;

//todo: NEU (ausgeklammert: Interfaces)
//todo: Neu (PresentListClickListener)
public class PresentsActivity extends AppCompatActivity implements PresentListClickListener/*PresentsAddFragment.OnListItemChangedListener, PresentsListFragment.OnListItemSelectedListener */{


    //todo: NEU (erstmal ausgeklammert: Interfaces)
    /**
    @Override
    public void onListItemSelected(int id) {
        //todo: evtl. anzeigen und dann ändern lassen, so in etwa:
        // PresentsAddFragment paf =
        // (PresentsAddFragment) getFragmentManager().findFragmentById(R.id.fragment_presents_add);
        // if (paf != null) {
        // paf.viewContent(id); <- muss noch implementiert werden
        // } else {
        // Toast.makeText(this, "Du wirst weitergeleitet.", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, PresentsAddActivity.class);
        // intent.putExtra(PresentsAddFragment.ARG_ID, id);
        // startActivity(intent);
    }
     */

    /**
    @Override
    public void onListItemChanged() {
        PresentsListFragment paf =
                (PresentsListFragment) getFragmentManager().findFragmentById(R.layout.fragment_presents_list);
        if (paf != null) {
            paf.populateList();
        }
        //Datenbank Bescheid geben?
    }
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presents);

        Button addButton = findViewById(R.id.button_addPresent);
        addButton.setOnClickListener(new View.OnClickListener() {
            //todo: die Textmeldungen noch rauslöschen: nur für uns intern, oder?
            @Override
            public void onClick(View v) {
                PresentsAddFragment paf =
                        (PresentsAddFragment) getFragmentManager().findFragmentById(R.layout.fragment_presents_add);
                if (paf != null) {
                    Toast.makeText(PresentsActivity.this, "Gib das neue Geschenk auf der rechten Seite ein.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PresentsActivity.this, "Du wirst weitergeleitet.",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PresentsActivity.this, PresentsAddActivity.class);
                    startActivity(intent);
                }
            }
        });


    }
    // todo: Neu (Test)
    //wird aufgerufen, wenn Item aus Liste angeklickt wurde
    @Override
    public void onUpdatePresentListItem(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status) {
        Log.d("PresentsActivity", "onClick_clicked");

        //https://developer.android.com/training/basics/fragments/communicating
        // direkter Aufruf der Fragment Methode möglich, wenn großer Bildschirm (keine Activity dazwischengeschaltet)
        //die verwenden getSupportFragmentManager?
        PresentsAddFragment paf =
                (PresentsAddFragment) getFragmentManager().findFragmentById(R.layout.fragment_presents_add);
        if (paf != null) {
            //todo: hier müsste Eingabe auch aktualisiert werden ?
            //todo: test
            //https://developer.android.com/training/basics/fragments/communicating
            // direkter Aufruf der Fragment Methode möglich, wenn großer Bildschirm (keine Activity dazwischengeschaltet)
            paf.onUpdatePresentItem(presentName, personFirstName, personLastName, eventName, price, shop, status);
        } else {
            //https://developer.android.com/training/basics/fragments/communicating
            // Argumente in Bundle packen zur Übergabe an Activity
            Bundle extras = new Bundle();
            extras.putString("presentNameString", presentName);
            extras.putString("personFirstNameString", personFirstName);
            extras.putString("personLastNameString", personLastName);
            extras.putString("eventNameString", eventName);
            extras.putString("priceString", price);
            extras.putString("shopNameString", shop);
            extras.putString("statusNameString", status);

            //todo: muss das ins Manifest?
            Intent intent = new Intent(PresentsActivity.this, PresentsAddActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

}