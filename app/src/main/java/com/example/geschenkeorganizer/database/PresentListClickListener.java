package com.example.geschenkeorganizer.database;

import android.view.View;

//todo: Neu
// https://medium.com/@apeapius/how-i-usually-code-recyclerview-adapter-class-65e30bcf30f
// Idee Verwendung Interface zur Kommunikation Fragment - Fragment (Sachen an MainActivity übergeben, da mittels Intent an AddFragment)
// Methoden komplett anders
public interface PresentListClickListener {
    void onUpdatePresentListItem(String presentName, String personFirstName, String personLastName, String eventName, String price, String shop, String status);
}
