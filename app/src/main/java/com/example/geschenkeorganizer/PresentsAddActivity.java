package com.example.geschenkeorganizer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class PresentsAddActivity extends AppCompatActivity implements PresentsAddFragment.OnListItemChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_presents);
    }

    @Override
    public void onListItemChanged() {
        //todo
    }

}