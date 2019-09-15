package com.example.geschenkeorganizer.presents;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.PresentListClickListener;

public class PresentsActivity extends FragmentActivity implements PresentListClickListener/*PresentsAddFragment.OnListItemChangedListener, PresentsListFragment.OnListItemSelectedListener */{

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

        /**Android Developers. (n.d.).
         * Build a flexible UI. Add a Fragment to an Activity at Runtime.
         * Retrieved from https://developer.android.com/training/basics/fragments/fragment-ui.
         * Fragment dynamisch zu Layout hinzufügen*/
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
                /**Android Developers. (n.d.).
                 * Communicate with other fragments. Deliver a Message to a Fragment.
                 * Retrieved from https://developer.android.com/training/basics/fragments/communicating.html*/
                PresentsAddFragment paf =
                        (PresentsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
                if (paf != null) {
                    //todo:Neu
                    // set Status + leeren Dialog laden
                    paf.setStatus(STATUS_ADD);
                    paf.loadEmptyAddView();
                } else {
                    //todo: NEU
                    // Durchführung Transaktion: Fragments austauschen
                    PresentsAddFragment presentsAddFragment = new PresentsAddFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

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

    //wird aufgerufen, wenn Item aus Liste angeklickt wurde
    @Override
    public void onPresentItemClick(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status) {
        Log.d("PresentsActivity", "onClick");
        /**Android Developers. (n.d.).
         * Communicate with other fragments. Deliver a Message to a Fragment.
         * Retrieved from https://developer.android.com/training/basics/fragments/communicating.html*/

        PresentsAddFragment paf =
                (PresentsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4);
        if (paf != null) {
            //öffentliche Methode von PresentsAddFragment direkt aufrufen
            paf.setStatus(STATUS_UPDATE);
            paf.onPresentUpdate(presentName, personFirstName, personLastName, eventName, price, shop, status);
            paf.setInformation();
        } else {
            PresentsAddFragment presentsAddFragment = new PresentsAddFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, presentsAddFragment);
            transaction.addToBackStack(null);
            // set Status + onPresentUpdate über commit
            presentsAddFragment.setStatus(STATUS_UPDATE);
            presentsAddFragment.onPresentUpdate(presentName, personFirstName, personLastName, eventName, price, shop, status);
            transaction.commit();
        }
    }
}