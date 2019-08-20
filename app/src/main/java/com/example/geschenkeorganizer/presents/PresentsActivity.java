package com.example.geschenkeorganizer.presents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.R;


public class PresentsActivity extends AppCompatActivity implements PresentsListFragment.OnListItemSelectedListener {

    @Override
    public void onListItemSelected(int id) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presents);

        Button addButton = findViewById(R.id.button_addPerson);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PresentsActivity.this, "Add Fragment not there, switching!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PresentsActivity.this, PresentsAddActivity.class);
                startActivity(intent);
            }
        });
    }
}