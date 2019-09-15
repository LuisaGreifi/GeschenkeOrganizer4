package com.example.geschenkeorganizer.PersonsFile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.geschenkeorganizer.R;

public class PersonsActivity extends FragmentActivity implements PersonEventListClickListener {

    //Konstante --> Unterscheidung, ob Eintrag hinzugefügt/geupdatet wird
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);
        initFragment(savedInstanceState);
        initButton();
    }

    private void initFragment(Bundle savedInstanceState) {
        /**Android Developers. (n.d.).
         * Build a flexible UI. Add a Fragment to an Activity at Runtime.
         * Retrieved from https://developer.android.com/training/basics/fragments/fragment-ui.
         * Fragment dynamisch zu Layout hinzufügen*/
        if (findViewById(R.id.persons_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            PersonsListFragment personsListFragment = new PersonsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.persons_fragment_container, personsListFragment).commit();
        }
    }

    private void initButton() {
        Button addButton = findViewById(R.id.button_addPerson);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**Android Developers. (n.d.).
                 * Communicate with other fragments. Deliver a Message to a Fragment.
                 * Retrieved from https://developer.android.com/training/basics/fragments/communicating.html*/
                PersonsAddFragment paf =
                        (PersonsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment6);
                if (paf != null) {
                    paf.setStatus(STATUS_ADD);
                    paf.loadEmptyAddView();
                } else {
                    // Durchführung Transaktion: Fragments austauschen
                    PersonsAddFragment personsAddFragment = new PersonsAddFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.persons_fragment_container, personsAddFragment);
                    transaction.addToBackStack(null);

                    personsAddFragment.setStatus(STATUS_ADD);
                    transaction.commit();
                }
            }
        });
    }

    //wird aufgerufen, wenn Item aus Liste angeklickt wurde
    @Override
    public void onPersonEventItemClicked(String personFirstName, String personLastName, String
            eventName, String eventDate) {
        /**Android Developers. (n.d.).
         * Communicate with other fragments. Deliver a Message to a Fragment.
         * Retrieved from https://developer.android.com/training/basics/fragments/communicating.html*/
        PersonsAddFragment paf =
                (PersonsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment6);
        if (paf != null) {

            //öffentliche Methode von PresentsAddFragment direkt aufrufen
            paf.setStatus(STATUS_UPDATE);
            paf.onPersonsUpdate(personFirstName, personLastName, eventName, eventDate);
            paf.setInformation();
        } else {
            PersonsAddFragment personsAddFragment = new PersonsAddFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.persons_fragment_container, personsAddFragment);
            transaction.addToBackStack(null);

            personsAddFragment.setStatus(STATUS_UPDATE);
            personsAddFragment.onPersonsUpdate(personFirstName, personLastName, eventName, eventDate);
            transaction.commit();
        }
    }
}