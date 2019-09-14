package com.example.geschenkeorganizer.PersonsFile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.geschenkeorganizer.R;
import com.example.geschenkeorganizer.database.PersonEventListClickListener;
import com.example.geschenkeorganizer.presents.PresentsAddFragment;
import com.example.geschenkeorganizer.presents.PresentsListFragment;

//todo: NEU (ausgeklammert: Interfaces)
//todo: Neu (PersonEventListListClickListener)
//todo: Neu: extends FragmentActivity
public class PersonsActivity extends FragmentActivity implements PersonEventListClickListener /**PersonsListFragment.OnListItemSelectedListener, PersonsAddFragment.OnListItemChangedListener */{

    //todo: Neu
    //Konstante --> Unterscheidung, ob Geschenk hinzugefügt/geupdatet wird
    private static final int STATUS_ADD = 0;
    private static final int STATUS_UPDATE = 1;


//todo: NEU (erstmal ausgeklammert: Interfaces)
/**
    @Override
    public void onListItemSelected(int id) {
        //todo: evtl. anzeigen und dann ändern lassen, so in etwa:
        // PersonsAddFragment paf =
        // (PersonsAddFragment) getFragmentManager().findFragmentById(R.id.fragment_persons_add);
        // if (paf != null) {
        // paf.viewContent(id); <- muss noch implementiert werden
        // } else {
        // Toast.makeText(this, "Du wirst weitergeleitet.", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, PersonsAddActivity.class);
        // intent.putExtra(PersonsAddFragment.ARG_ID, id);
        // startActivity(intent);
    }
 */
//todo: NEU (erstmal ausgeklammert: Interfaces)
/**
    @Override
    public void onListItemChanged() {
        PersonsListFragment paf =
                (PersonsListFragment) getFragmentManager().findFragmentById(R.layout.fragment_persons_list);
        if (paf != null) {
            paf.populateList();
        }
        //Datenbank Bescheid geben?
    }
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        //https://developer.android.com/training/basics/fragments/fragment-ui
        //Fragment hinzufügen zu Layout
        if (findViewById(R.id.persons_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            PersonsListFragment personsListFragment = new PersonsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.persons_fragment_container, personsListFragment).commit();
        }

            Button addButton = findViewById(R.id.button_addPerson);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonsAddFragment paf =
                            (PersonsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment6);
                    if (paf != null) {
                        //todo:Neu
                        // set Status + leeren Dialog laden
                        paf.setStatus(STATUS_ADD);
                        paf.loadEmptyAddView();
                    } else {
                        //todo: NEU
                        // https://developer.android.com/training/basics/fragments/communicating
                        PersonsAddFragment personsAddFragment = new PersonsAddFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        //todo: funktioniert nur, wenn Fragment dynamisch hinzugefügt zu Layout
                        transaction.replace(R.id.persons_fragment_container, personsAddFragment);
                        transaction.addToBackStack(null);
                        //todo:Neu
                        // set Status
                        personsAddFragment.setStatus(STATUS_ADD);
                        transaction.commit();
                    }
                }
            });
        }

        //todo: neu
        //wird aufgerufen, wenn Item aus Liste angeklickt wurde
        @Override
        public void onPersonEventItemClicked(String personFirstName, String personLastName, String
        eventName, String eventDate){
            //todo Neu
            //https://developer.android.com/training/basics/fragments/communicating
            //quasi von da übernommen (angepasst)
            PersonsAddFragment paf =
                    (PersonsAddFragment) getSupportFragmentManager().findFragmentById(R.id.fragment6);
            if (paf != null) {
                //todo: NEU
                //https://developer.android.com/training/basics/fragments/communicating.html
                //öffentliche Methode von PresentsAddFragment direkt aufrufen
                //todo: Neu
                paf.setStatus(STATUS_UPDATE);
                paf.onPersonsUpdate(personFirstName, personLastName, eventName, eventDate);
                //todo: hier evntl auch Button anpassen (setInformation)
                paf.setInformation();
            } else {
                //todo: NEU
                // https://developer.android.com/training/basics/fragments/communicating
                PersonsAddFragment personsAddFragment = new PersonsAddFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.persons_fragment_container, personsAddFragment);
                transaction.addToBackStack(null);
                //todo:Neu
                // set Status + onPresentUpdate über commit
                personsAddFragment.setStatus(STATUS_UPDATE);
                personsAddFragment.onPersonsUpdate(personFirstName, personLastName, eventName, eventDate);
                transaction.commit();
            }
        }
}