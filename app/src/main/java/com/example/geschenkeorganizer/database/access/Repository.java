package com.example.geschenkeorganizer.database.access;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import com.example.geschenkeorganizer.PersonsFile.PersonsAddListener;
import com.example.geschenkeorganizer.database.entities.Event;
import com.example.geschenkeorganizer.database.MyDatabase;
import com.example.geschenkeorganizer.database.entities.Person;
import com.example.geschenkeorganizer.database.entities.PersonEventJoin;
import com.example.geschenkeorganizer.database.representations.PersonEventRepresentation;
import com.example.geschenkeorganizer.database.entities.Present;
import com.example.geschenkeorganizer.database.representations.PresentRepresentation;
import com.example.geschenkeorganizer.presents.PresentsAddListener;

import java.util.List;

/**Google Developers Codelabs. (n.d.).
 * Create the Repository. Implementing the Repository.
 * Retrieved from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7.
 * grundlegende Erstellung Repository */

public class Repository {

    private MyDatabase myDatabase;

    private LiveData<List<PresentRepresentation>> allPresents;
    private LiveData<List<PersonEventRepresentation>> allPersonsEvents;

    private PresentsAddListener presentsListener;
    private PersonsAddListener personsListener;

    /**Murthy, A. (04.05.2018).
     * 5 steps to implement Room persistence library in Android.
     * Retrieved from https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24.
     * Verwendung context */
    public Repository(Context context){
        myDatabase = MyDatabase.getDatabase(context);

        allPresents = myDatabase.daoAccess().getAllPresentsForRepresentation();
        allPersonsEvents = myDatabase.daoAccess().getAllPersonsWithEventsForRepresentation();
    }


    //Person und Event hinzufügen Dialog

    //Person hinzufügen

    /**Murthy, A. (04.05.2018).
     * 5 steps to implement Room persistence library in Android.
     * Retrieved from https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24.
     * Funktionsweise der Insert-Methode im Repository*/
    public void insertPersonEvent(final String firstName, final String lastName, final String eventName, final int eventDate){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                insertPerson(firstName, lastName);
                insertEvent(eventName, eventDate);
                insertPersonEventConnection(firstName, lastName, eventName, eventDate);
                return null;
            }

            protected void onPostExecute(Void result) {
                personsListener.onPostAddPerson();
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

    private void insertEvent(String eventName, int eventDate){
        if (!myDatabase.daoAccess().existsEventWithEventInformationAlready(eventName, eventDate)) {
            Event event = new Event();
            event.setEventName(eventName);
            event.setEventDate(eventDate);
            myDatabase.daoAccess().insertEvent(event);
        }
    }

    private void insertPersonEventConnection(String firstName, String lastName, String eventName, int eventDate){
        int personId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);
        int eventId = myDatabase.daoAccess().getEventIdByEventInformation(eventName, eventDate);
        if (!myDatabase.daoAccess().existsPersonEventConnectionAlready(personId, eventId)) {
            PersonEventJoin personEventJoin = new PersonEventJoin();
            personEventJoin.setPersonId(personId);
            personEventJoin.setEventId(eventId);
            myDatabase.daoAccess().insertPersonEventJoin(personEventJoin);
        }
    }

    // Eintrag in Personen-Event-Liste aktualisieren

    public void updatePersonEvent(final String personFirstNameToUpdate, final String personLastNameToUpdate, final String eventNameToUpdate, final int eventDateToUpdate, final String personFirstName, final String personLastName, final String eventName, final int eventDate){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                updatePerson(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, eventDateToUpdate, personFirstName, personLastName, eventName, eventDate);
                updateEvent(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, eventDateToUpdate, personFirstName, personLastName, eventName, eventDate);

                return null;
            }

            protected void onPostExecute(Void result) {
                personsListener.onPostUpdatePerson();
            }
        }.execute();
    }

    private void updatePerson(String personFirstNameToUpdate, String personLastNameToUpdate, String eventNameToUpdate, int eventDateToUpdate, String personFirstName, String personLastName, String eventName, int eventDate){
        if((!personFirstNameToUpdate.equals(personFirstName)) || (!personLastNameToUpdate.equals(personLastName))){
            insertPerson(personFirstName, personLastName);
            deleteOldPersonEventConnection(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, eventDateToUpdate);
            updatePersonEventConnectionForUpdatedPerson(eventNameToUpdate, eventDateToUpdate, personFirstName, personLastName, eventName, eventDate);
        }
    }

    private void deleteOldPersonEventConnection(String personFirstNameToUpdate, String personLastNameToUpdate, String eventNameToUpdate, int eventDateToUpdate){
        int oldPersonId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);
        int oldEventId = myDatabase.daoAccess().getEventIdByEventInformation(eventNameToUpdate, eventDateToUpdate);

        myDatabase.daoAccess().deletePersonEventJoin(oldPersonId, oldEventId);
    }

    private void updatePersonEventConnectionForUpdatedPerson(String eventNameToUpdate, int eventDateToUpdate, String personFirstName, String personLastName, String eventName, int eventDate){
        if((eventNameToUpdate.equals(eventName)) && (eventDateToUpdate == eventDate)){
            insertPersonEventConnection(personFirstName, personLastName, eventNameToUpdate, eventDateToUpdate);
        }else{
            insertEvent(eventName, eventDate);
            insertPersonEventConnection(personFirstName, personLastName, eventName, eventDate);
        }
    }

    private void updateEvent(String personFirstNameToUpdate, String personLastNameToUpdate, String eventNameToUpdate, int eventDateToUpdate, String personFirstName, String personLastName, String eventName, int eventDate){
        if((!eventNameToUpdate.equals(eventName)) || (eventDateToUpdate != eventDate)){
            insertEvent(eventName, eventDate);
            deleteOldPersonEventConnection(personFirstNameToUpdate, personLastNameToUpdate, eventNameToUpdate, eventDateToUpdate);
            updatePersonEventConnectionForUpdatedEvent(personFirstNameToUpdate, personLastNameToUpdate, personFirstName, personLastName, eventName, eventDate);
        }
    }

    private void updatePersonEventConnectionForUpdatedEvent(String personFirstNameToUpdate, String personLastNameToUpdate, String personFirstName, String personLastName, String eventName, int eventDate){
        if((personFirstNameToUpdate.equals(personFirstName)) && (personLastNameToUpdate.equals(personLastName))){
            insertPersonEventConnection(personFirstNameToUpdate, personLastNameToUpdate, eventName, eventDate);
        }else{
            insertPerson(personFirstName, personLastName);
            insertPersonEventConnection(personFirstName, personLastName, eventName, eventDate);
        }
    }


    // Geschenk hinzufügen Dialog

    // Geschenk hinzufügen

    public void insertPresent(final String firstName, final String lastName, final String eventName, final String presentName, final double presentPrice, final String presentShop, final String presentStatus) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                insertPerson(firstName, lastName);
                int personId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);
                insertEventForPresent(personId, eventName);
                int eventId = getEventIdForPresent(personId, eventName);
                insertPersonEventConnectionForPresent(personId, eventId);
                insertPresentWithData(personId, eventId, presentName, presentPrice, presentShop, presentStatus);
                return null;
            }

            protected void onPostExecute(Void result) {
                presentsListener.onPostAddPresent();
            }
        }.execute();


    }

    private void insertEventForPresent(int personId, String eventName){
        if (!myDatabase.daoAccess().existsEventForPersonAlready(personId, eventName) && !myDatabase.daoAccess().existsEventWithEventInformationAlready(eventName, 0)) {
            Event event = new Event();
            event.setEventName(eventName);
            event.setEventDate(0);
            myDatabase.daoAccess().insertEvent(event);
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

    // Geschenk aktualisieren
    public void updatePresent(final String personFirstNameToUpdate, final String personLastNameToUpdate, final String eventToUpdate, final String presentNameToUpdate, final double priceToUpdate, final String shopToUpdate, final String statusToUpdate, final String personFirstName, final String personLastName, final String eventName, final String presentName, final double presentPrice, final String presentShop, final String presentStatus){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int personId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);
                Present present = getPresentByPresentInformation(personId, presentNameToUpdate,priceToUpdate,shopToUpdate,statusToUpdate);

                //neue Personen Id wird hinzugefügt und gegebenenfalls erstellt, wenn sich der eingegebene Name zum gespeicherten Namen unterscheidet
                if((!personFirstNameToUpdate.equals(personFirstName)) || (!personLastNameToUpdate.equals(personLastName))){
                    insertPerson(personFirstName, personLastName);
                    int newPersonId = myDatabase.daoAccess().getPersonIdByName(personFirstName, personLastName);
                    present.setPersonId(newPersonId);
                }

                //neue EventId wird hinzugefügt und gegebenenfalls erstellt, wenn sich der eingegebene Eventname zum gespeicherten Eventnamen unterscheidet
                if(!eventToUpdate.equals(eventName)){
                    int eventId = updateEventForUpdatedPresent(personFirstNameToUpdate, personLastNameToUpdate, eventToUpdate, personFirstName, personLastName, eventName);
                    present.setEventId(eventId);
                }

                //neuer Geschenkname wird hinzugefügt, wenn sich der eingegebene Geschenkename zum gespeicherten Geschenknamen unterscheidet
                if(!presentName.equals(presentNameToUpdate)){
                    present.setPresentName(presentName);
                }

                //neuer Preis wird hinzugefügt, wenn sich der eingegebene Preis zum gespeicherten Preis unterscheidet
                if(presentPrice != priceToUpdate){
                    present.setPrice(presentPrice);
                }

                //neuer Einkaufsort wird hinzugefügt, wenn sich der eingegebene Einkaufsort zum gespeicherten Einkaufsort unterscheidet
                if(!presentShop.equals(shopToUpdate)){
                    present.setShop(presentShop);
                }

                //neuer Status wird hinzugefügt, wenn sich der eingegebene Status zum gespeicherten Status unterscheidet
                if(!presentStatus.equals(statusToUpdate)){
                    present.setStatus(presentStatus);
                }

                myDatabase.daoAccess().updatePresent(present);

                return null;
            }

            protected void onPostExecute(Void result) {
                presentsListener.onPostUpdatePresent();
            }
        }.execute();
    }

    private int updateEventForUpdatedPresent(String personFirstNameToUpdate, String personLastNameToUpdate, String eventToUpdate, String personFirstName, String personLastName, String eventName){
        int eventId;
        int personId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);

        if((personFirstNameToUpdate.equals(personFirstName)) && (personLastNameToUpdate.equals(personLastName))){
            insertEventForPresent(personId, eventName);
            eventId = getEventIdForPresent(personId, eventName);
            insertPersonEventConnectionForPresent(personId, eventId);
        }else{
            int newPersonId = myDatabase.daoAccess().getPersonIdByName(personFirstName, personLastName);
            insertEventForPresent(newPersonId, eventName);
            eventId = getEventIdForPresent(newPersonId, eventName);
            insertPersonEventConnectionForPresent(newPersonId, eventId);
        }
        return eventId;
    }

    private Present getPresentByPresentInformation(int personId, String presentNameToUpdate, double priceToUpdateDouble, String shopToUpdate, String statusToUpdate){
        Present presentToUpdate;
        presentToUpdate = myDatabase.daoAccess().getPresentByPresentInformation(personId, presentNameToUpdate, priceToUpdateDouble, shopToUpdate, statusToUpdate);
        return presentToUpdate;
    }


    // Present Repräsentation
    LiveData<List<PresentRepresentation>> getAllPresents() {
        return allPresents;
    }

    // Person Event Repräsentation
    LiveData<List<PersonEventRepresentation>> getAllPersonsWithEvents() {
        return allPersonsEvents;
    }

    /**Creating Custom Listeners. (n.d.).
     * Retrieved from https://guides.codepath.com/android/Creating-Custom-Listeners#2-create-listener-setter.
     * Verwendung von setListener zur Anmeldung des Listeners */
    public void setPresentsAddListener(PresentsAddListener listener){
        this.presentsListener = listener;
    }

    public void setPersonsAddListener(PersonsAddListener listener){
        this.personsListener = listener;
    }
}
