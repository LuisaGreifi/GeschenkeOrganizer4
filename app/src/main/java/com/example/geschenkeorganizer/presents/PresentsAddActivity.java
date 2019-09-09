package com.example.geschenkeorganizer.presents;

import android.content.Intent;
import android.os.Bundle;
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