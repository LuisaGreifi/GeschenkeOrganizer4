package com.example.geschenkeorganizer.PersonsFile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.R;

public class PersonsActivity extends AppCompatActivity implements  PersonsListFragment.OnListItemSelectedListener, PersonsAddFragment.OnListItemChangedListener {

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

    @Override
    public void onListItemChanged() {
        PersonsListFragment paf =
                (PersonsListFragment) getFragmentManager().findFragmentById(R.layout.fragment_persons_list);
        if (paf != null) {
            paf.populateList();
        }
        //Datenbank Bescheid geben?
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        Button addButton = findViewById(R.id.button_addPerson);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonsAddFragment paf =
                        (PersonsAddFragment) getFragmentManager().findFragmentById(R.layout.fragment_persons_add);
                if (paf != null) {
                    Toast.makeText(PersonsActivity.this, "Gib die neue Person auf der rechten Seite ein.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonsActivity.this, "Du wirst weitergeleitet.",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PersonsActivity.this, PersonsAddActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}