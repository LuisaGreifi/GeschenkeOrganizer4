package com.example.geschenkeorganizer.database;

public interface PresentListClickListener {
    void onPresentItemClick(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status);
}
