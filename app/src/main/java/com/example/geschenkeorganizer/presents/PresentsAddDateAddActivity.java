package com.example.geschenkeorganizer.presents;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geschenkeorganizer.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PresentsAddDateAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editText_eventDate;
    private String eventDate;
    private int eventDateDay, eventDateMonth;
    private Button button_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presents_add_date_add);
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(eventDate!=null) {
                    saveEntry();
                } else {
                    Toast.makeText(PresentsAddDateAddActivity.this, "Du hast kein Datum gespeichert.",
                            Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(PresentsAddDateAddActivity.this, PresentsActivity.class);
                startActivity(i);
            }
        });
        editText_eventDate = findViewById(R.id.editText_eventDate);
        initEventDate();
    }

    private void saveEntry() {
        eventDate = editText_eventDate.getText().toString();
        eventDateDay = Integer.getInteger(eventDate.substring(0,2));
        eventDateMonth = Integer.getInteger(eventDate.substring(3, 5));
        //todo: in Datenbank (eventDateDay als int und eventDateMonth als int)
    }

    private void initEventDate() {
        editText_eventDate.setFocusable(false);
        editText_eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog().show();
            }
        });
    }

    private DatePickerDialog createDatePickerDialog() {
        GregorianCalendar today = new GregorianCalendar();
        int day = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        return new DatePickerDialog(this, this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        GregorianCalendar date = new GregorianCalendar(year, month, dayOfMonth);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        eventDate = df.format(date.getTime());
        editText_eventDate.setText(eventDate);
    }
}
