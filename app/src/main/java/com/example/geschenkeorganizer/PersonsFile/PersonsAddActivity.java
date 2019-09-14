package com.example.geschenkeorganizer.PersonsFile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.R;

//todo: Neu: Klasse nicht verwenden! (kann gelöscht werden)
// Fragment in PersonsActivity austauschen
// -> Daten überlieferbar, sonst (HIER) eher nicht

public class PersonsAddActivity extends AppCompatActivity implements PersonsAddFragment.OnListItemChangedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_persons);
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "Erstelle das neue Geschenk auf der rechten Seite.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemChanged() {
        //todo: Datenbank Bescheid geben?
    }
}
