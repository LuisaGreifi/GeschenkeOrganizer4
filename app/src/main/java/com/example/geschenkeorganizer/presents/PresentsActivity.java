package com.example.geschenkeorganizer.presents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.PresentListClickListener;

//todo: NEU (ausgeklammert: Interfaces)
//todo: Neu (PresentListClickListener)
//todo: Neu: extends FragmentActivity
public class PresentsActivity extends FragmentActivity implements PresentListClickListener/*PresentsAddFragment.OnListItemChangedListener, PresentsListFragment.OnListItemSelectedListener */{

    //todo: Neu
    //Konstante --> Unterscheidung, ob Geschenk hinzugefügt/geupdatet wird
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;

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

        //https://developer.android.com/training/basics/fragments/fragment-ui
        //Fragment hinzufügen zu Layout
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            PresentsListFragment presentsListFragment = new PresentsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, presentsListFragment).commit();
        }

        Button addButton = findViewById(R.id.button_addPresent);
        addButton.setOnClickListener(new View.OnClickListener() {
            //todo: die Textmeldungen noch rauslöschen: nur für uns intern, oder?

            //todo Neu
            //https://developer.android.com/training/basics/fragments/communicating
            //quasi von da übernommen
            @Override
            public void onClick(View v) {
                PresentsAddFragment paf =
                        (PresentsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
                if (paf != null) {
                    //todo:Neu
                    // set Status + leeren Dialog laden
                    paf.setStatus(STATUS_ADD);
                    paf.loadEmptyAddView();
                } else {
                    //todo: NEU
                    // https://developer.android.com/training/basics/fragments/communicating
                    PresentsAddFragment presentsAddFragment = new PresentsAddFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    //todo: funktioniert nur, wenn Fragment dynamisch hinzugefügt zu Layout
                    transaction.replace(R.id.fragment_container, presentsAddFragment);
                    transaction.addToBackStack(null);
                    //todo:Neu
                    // set Status
                    presentsAddFragment.setStatus(STATUS_ADD);

                    transaction.commit();
                }
            }
        });


    }

    //todo: neu
    //wird aufgerufen, wenn Item aus Liste angeklickt wurde
    @Override
    public void onPresentItemClick(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status) {
        Log.d("PresentsActivity", "onClick");

        //todo Neu
        //https://developer.android.com/training/basics/fragments/communicating
        //quasi von da übernommen (angepasst)
        PresentsAddFragment paf =
                (PresentsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
        if (paf != null) {
            //todo: NEU
            //https://developer.android.com/training/basics/fragments/communicating.html
            //öffentliche Methode von PresentsAddFragment direkt aufrufen
            //todo: Neu
            paf.setStatus(STATUS_UPDATE);
            paf.onPresentUpdate(presentName, personFirstName, personLastName, eventName, price, shop, status);
            //todo: hier evntl auch Button anpassen (setInformation)
            paf.setInformation();
        } else {
            //todo: NEU
            // https://developer.android.com/training/basics/fragments/communicating
            PresentsAddFragment presentsAddFragment = new PresentsAddFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, presentsAddFragment);
            transaction.addToBackStack(null);
            //todo:Neu
            // set Status + onPresentUpdate über commit
            presentsAddFragment.setStatus(STATUS_UPDATE);
            presentsAddFragment.onPresentUpdate(presentName, personFirstName, personLastName, eventName, price, shop, status);
            transaction.commit();
        }
    }
}