package com.example.geschenkeorganizer.persons;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.R;

public class PersonsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        Button addButton = findViewById(R.id.button_addPerson);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonsActivity.this, "Add Fragment not there, switching!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PersonsActivity.this, PersonsAddActivity.class);
                startActivity(intent);
            }
        });
    }

}
