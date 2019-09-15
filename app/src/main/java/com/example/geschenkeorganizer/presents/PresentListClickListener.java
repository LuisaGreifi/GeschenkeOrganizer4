package com.example.geschenkeorganizer.presents;

public interface PresentListClickListener {
    void onPresentItemClick(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status);
}
