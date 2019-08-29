package com.example.geschenkeorganizer.database;

import android.content.Context;
import android.os.AsyncTask;

public class Repository {

    // https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24
    private MyDatabase myDatabase;
    public Repository(Context context){
        // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7
        myDatabase = MyDatabase.getDatabase(context);
    }

    //Person une Event hinzufügen Dialog

    //vorsicht: Felder, die nicht leer sein dürfen, müssen davor noch gecheckt werden (Button Klick -- > falls Feld leer, dass nicht leer sein darf: Meldung --> inserten)
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

    //todo: in diesem Fall MUSS der Nutzer EventName + -datum eingeben?
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

    public void insertPresent(final String firstName, final String lastName, final String eventName, final String presentName, final double presentPrice, final String presentShop, final String presentStatus) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                insertPerson(firstName, lastName);
                //todo: evntl auslagern
                int personId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);

                insertEventForPresent(personId, eventName);
                //todo: evntl. auslagern
                //todo: funktioniert nachhad a bloß, wenn Person-Event Beziehung schon exisitert (if(Event für Person+EventName exisitert schon)( --> getEventDate)else( --> EventDate=0) --> eventId anhand Daten
                int eventId;
                int eventDate = myDatabase.daoAccess().getEventDateForPersonAndEventName(personId, eventName);
                eventId = myDatabase.daoAccess().getEventIdByEventInformation(eventName, eventDate);

                //todo: evntl. mit Merhode oben zusammenlegen
                insertPersonEventConnectionForPresent(personId, eventId);
                insertPresentWithData(personId, eventId, presentName, presentPrice, presentShop, presentStatus);
                return null;
            }
        }.execute();
    }

    private void insertEventForPresent(int personId, String eventName){
        //todo: obacht: das eine Zeile unique ist kann so nicht eingehalten werden (Anlass - null) (bei:  if (!myDatabase.daoAccess().existsEventForPersonAlready(personId, eventName)))
        //todo: nicht einfach so per id updatebar --> bezieht sich ja auf 1 Person (NICHT einfach Eventdatum ändern)
        //todo: Lösungsvorschlag: vllt doch Spinner mit Array aus bereits existierenden Events (KOMPLIZIERT!)
        //todo: Lösungsvorschlag: update umständlicher gestalten (eventId anhand Name und Personen Infos bekommen --> neues Event --> eventName altes Event getten + für neues setten --> eingegebenes Datum für Event setten --> Event inserten --> Person inserten --> PersonEventJoin inserten --> alten PersonEventJoin löschen)
        //todo: mit 0 semi-elegante Lösung, aber sollte funktionieren
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
}
