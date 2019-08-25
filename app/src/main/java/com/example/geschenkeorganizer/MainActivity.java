package com.example.geschenkeorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.PersonsFile.PersonsActivity;
import com.example.geschenkeorganizer.presents.PresentsActivity;

public class MainActivity extends AppCompatActivity {

    //Variablen und Konstanten

    Button presentsButton;
    Button personsButton;
    Button inspirationButton;

    //onCreate()-Methode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }

    private void findViews() {
        presentsButton = findViewById(R.id.button_presents);
        personsButton = findViewById(R.id.button_persons);
        inspirationButton = findViewById(R.id.button_inspiration);
    }

    private void initViews() {
        initPresentsButton();
        initPersonsButton();
        initInspirationButton();
    }

    private void initPresentsButton() {
        presentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PresentsActivity.class);
                startActivity(i);
            }
        });
    }

    private void initPersonsButton() {
        personsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PersonsActivity.class);
                startActivity(i);
            }
        });
    }

    private void initInspirationButton() {
        inspirationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, InspirationActivity.class);
                startActivity(i);
            }
        });
    }
}
