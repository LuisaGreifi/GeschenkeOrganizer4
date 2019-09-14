package com.example.geschenkeorganizer.database;

//todo: Neu
// https://medium.com/@apeapius/how-i-usually-code-recyclerview-adapter-class-65e30bcf30f
// Idee Verwendung Interface zur Kommunikation Fragment - Fragment (Sachen an MainActivity übergeben, da mittels Intent an AddFragment)
// Methoden komplett anders
public interface PersonEventListClickListener {
    void onPersonEventItemClicked(String personFirstName, String personLastName, String eventName, String eventDate);
}
