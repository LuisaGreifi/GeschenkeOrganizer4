package com.example.geschenkeorganizer.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    //todo: NEU
    // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
    private LiveData<List<PresentRepresentation>> allPresents;
    private LiveData<List<PersonEventRepresentation>> allPersonsEvents;

    // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
    private MyDatabase myDatabase;

    public Repository(Context context){
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
        myDatabase = MyDatabase.getDatabase(context);

        //todo: NEU
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
        allPresents = myDatabase.daoAccess().getAllPresentsForRepresentation();
        allPersonsEvents = myDatabase.daoAccess().getAllPersonsWithEventsForRepresentation();
    }

    //Person und Event hinzufügen Dialog

    //todo: vorsicht: Felder, die nicht leer sein dürfen, müssen davor noch gecheckt werden (Button Klick -- > falls Feld leer, dass nicht leer sein darf: Meldung --> inserten)

    // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
    //Funktionsweise
    public void insertPersonEvent(final String firstName, final String lastName, final String eventName, final int eventDate){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                insertPerson(firstName, lastName);
                insertEvent(eventName, eventDate);
                insertPersonEventConnection(firstName, lastName, eventName, eventDate);
                return null;
            }
        }.execute();
    }

    private void insertPerson(String firstName, String lastName){
        if (!myDatabase.daoAccess().existsPersonWithNameAlready(firstName, lastName)){
            Person person = new Person();
            person.setFirstName(firstName);
            person.setLastName(lastName);
            myDatabase.daoAccess().insertPerson(person);
        }
    }

    //todo: in diesem Fall MUSS der Nutzer EventName + -datum eingeben? --> noch in UI Nutzer informieren!
    private void insertEvent(String eventName, int eventDate){
        if (!myDatabase.daoAccess().existsEventWithEventInformationAlready(eventName, eventDate)) {
            Event event = new Event();
            event.setEventName(eventName);
            event.setEventDate(eventDate);
            myDatabase.daoAccess().insertEvent(event);
        }
    }

    private void insertPersonEventConnection(String firstName, String lastName, String eventName, int eventDate){
        // brauch die zugehörige Id ja auch, wenn 1 von beiden schon existiert!
        int personId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);
        int eventId = myDatabase.daoAccess().getEventIdByEventInformation(eventName, eventDate);
        if (!myDatabase.daoAccess().existsPersonEventConnectionAlready(personId, eventId)) {
            PersonEventJoin personEventJoin = new PersonEventJoin();
            personEventJoin.setPersonId(personId);
            personEventJoin.setEventId(eventId);
            myDatabase.daoAccess().insertPersonEventJoin(personEventJoin);
        }
    }




    // Geschenk hinzufügen Dialog

    //todo: Neu (eventId bekommen geändert --> TESTEN)
    public void insertPresent(final String firstName, final String lastName, final String eventName, final String presentName, final double presentPrice, final String presentShop, final String presentStatus) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                insertPerson(firstName, lastName);
                int personId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);
                insertEventForPresent(personId, eventName);
                int eventId = getEventIdForPresent(personId, eventName);
                //todo: evntl. mit Methode oben zusammenlegen
                insertPersonEventConnectionForPresent(personId, eventId);
                insertPresentWithData(personId, eventId, presentName, presentPrice, presentShop, presentStatus);
                return null;
            }
        }.execute();
    }

    private void insertEventForPresent(int personId, String eventName){
        //todo: nicht einfach so per id updatebar --> Event bezieht sich ja nicht nur auf 1 Person (NICHT einfach Eventdatum ändern)
        //todo: Lösungsvorschlag: update umständlicher gestalten (eventId anhand Name und Personen Infos bekommen --> neues Event (wenn Eventdate = 0) --> eventName altes Event getten + für neues setten --> eingegebenes Datum für Event setten --> Event inserten (--> Person inserten) --> PersonEventJoin inserten --> alten PersonEventJoin löschen)
        //todo: mit 0 semi-elegante Lösung, aber sollte funktionieren (vllt in UI Eventdatum nicht anzeigen, wenn Date=0?
        if (!myDatabase.daoAccess().existsEventForPersonAlready(personId, eventName) && !myDatabase.daoAccess().existsEventWithEventInformationAlready(eventName, 0)) {
            Event event = new Event();
            event.setEventName(eventName);
            event.setEventDate(0);
            myDatabase.daoAccess().insertEvent(event);
            //dann muss Mini-Acivity geöffnet werden --> (nach Speichern, gleiche Methode zur Abfrage nutzen?)
            //--> wahrscheinlich hast du wieder die Ehre mit onPostExecute
            // --> Intent zu Mini-Activity
        }
    }

    private int getEventIdForPresent(int personId, String eventName){
        int eventId;
        int eventDate;
        if(myDatabase.daoAccess().existsEventForPersonAlready(personId, eventName)){
            eventDate = myDatabase.daoAccess().getEventDateForPersonAndEventName(personId, eventName);
        } else {
            eventDate = 0;
        }
        eventId = myDatabase.daoAccess().getEventIdByEventInformation(eventName, eventDate);
        return eventId;
    }

    private void insertPersonEventConnectionForPresent(int personId, int eventId){
        if (!myDatabase.daoAccess().existsPersonEventConnectionAlready(personId, eventId)) {
            PersonEventJoin personEventJoin = new PersonEventJoin();
            personEventJoin.setPersonId(personId);
            personEventJoin.setEventId(eventId);
            myDatabase.daoAccess().insertPersonEventJoin(personEventJoin);
        }
    }

    private void insertPresentWithData(int personId, int eventId, String presentName, double presentPrice, String presentShop, String presentStatus){
        Present present = new Present();
        present.setPersonId(personId);
        present.setEventId(eventId);
        present.setPresentName(presentName);
        present.setPrice(presentPrice);
        present.setShop(presentShop);
        present.setStatus(presentStatus);

        myDatabase.daoAccess().insertPresent(present);

    }


    //todo: NEU (Test)
    //1. Teil: alte von zu upzudateten Present, 2. Teil: Werte aus EditText
    public void updatePresent(final String personFirstNameToUpdate, final String personLastNameToUpdate, String eventToUpdate, final String presentNameToUpdate, final double priceToUpdate, final String shopToUpdate, final String statusToUpdate, final String firstName, final String lastName, final String eventName, final String presentName, final double presentPrice, final String presentShop, final String presentStatus){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Present present = getPresentByPresentInformation(personFirstNameToUpdate,personLastNameToUpdate, presentNameToUpdate,priceToUpdate,shopToUpdate,statusToUpdate);

                //todo: person + event
                if(presentName != presentNameToUpdate){
                    present.setPresentName(presentName);
                }
                if(presentPrice != priceToUpdate){
                    present.setPrice(presentPrice);
                }
                if(presentShop != shopToUpdate){
                    present.setShop(presentShop);
                }
                if(presentStatus != statusToUpdate){
                    present.setStatus(presentStatus);
                }

                myDatabase.daoAccess().updatePresent(present);

                return null;
            }
        }.execute();
    }

    //todo:NEU
    //todo: eventName fehlt
    private Present getPresentByPresentInformation(String personFirstNameToUpdate, String personLastNameToUpdate, String presentNameToUpdate, double priceToUpdateDouble, String shopToUpdate, String statusToUpdate){
        Present presentToUpdate;
        int personId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);
        presentToUpdate = myDatabase.daoAccess().getPresentByPresentInformation(personId, presentNameToUpdate, priceToUpdateDouble, shopToUpdate, statusToUpdate);
        return presentToUpdate;
    }



    //todo: NEU
    // Present Representation

    //https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
    LiveData<List<PresentRepresentation>> getAllPresents() {
        return allPresents;
    }



    //todo: NEU
    // Person Event Representation

    //https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
    LiveData<List<PersonEventRepresentation>> getAllPersonsWithEvents() {
        return allPersonsEvents;
    }

}
