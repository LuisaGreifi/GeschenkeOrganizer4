package com.example.geschenkeorganizer.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import com.example.geschenkeorganizer.PersonsFile.PersonsAddListener;
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

    //todo: NEU
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

            //todo:NEU
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

    //1. Teil: alte von zu upzudateten Present, 2. Teil: Werte aus EditText
    public void updatePersonEvent(final String personFirstNameToUpdate, final String personLastNameToUpdate, final String eventNameToUpdate, final int eventDateToUpdate, final String firstName, final String lastName, final String eventName, final int eventDate){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int personId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);
                Person person = myDatabase.daoAccess().getPersonById(personId);

                if((personFirstNameToUpdate != firstName) || (personLastNameToUpdate != lastName)){
                    insertPerson(firstName, lastName);
                    int newPersonId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);
                    int oldPersonId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);
                    int oldEventId = myDatabase.daoAccess().getEventIdByEventInformation(eventNameToUpdate, eventDateToUpdate);

                    //Connection bzw. Listeneintrag löschen
                    myDatabase.daoAccess().deletePersonEventJoin(oldPersonId, oldEventId);

                    //richtige Connection herstellen
                    if((eventNameToUpdate == eventName) && (eventDateToUpdate == eventDate)){
                        insertPersonEventConnection(firstName, lastName, eventNameToUpdate, eventDateToUpdate);
                    }else{
                        insertEvent(eventName, eventDate);
                        insertPersonEventConnection(firstName, lastName, eventName, eventDate);
                    }

                    //todo: wäre schon ganz cool
                    /**
                    //alte Person löschen, wenn es nur 1 Event dazu gab
                    List<Event> eventsForPerson = myDatabase.daoAccess().getEventForPerson(oldPersonId);
                    if(eventsForPerson.size() == 1){
                        myDatabase.daoAccess().deletePersonByName(personFirstNameToUpdate, personLastNameToUpdate);
                    }
                     */
                }

                if((eventNameToUpdate != eventName) || (eventDateToUpdate != eventDate)){
                    insertEvent(eventName, eventDate);

                    int oldEventId = myDatabase.daoAccess().getEventIdByEventInformation(eventNameToUpdate, eventDateToUpdate);
                    int oldPersonId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);

                    //alte Verbindung aka Listenanzeife löschen
                    myDatabase.daoAccess().deletePersonEventJoin(oldPersonId, oldEventId);

                    //richtige Connection herstellen
                    if((personFirstNameToUpdate == firstName) && (personLastNameToUpdate == lastName)){
                        insertPersonEventConnection(personFirstNameToUpdate, personLastNameToUpdate, eventName, eventDate);
                    }else{
                        insertPerson(firstName, lastName);
                        insertPersonEventConnection(firstName, lastName, eventName, eventDate);
                    }

                    //todo: wäre schon ganz cool
                    /**
                    //altes Event löschen, wenn es nur 1 Person dazu gab
                    List<Person> personsForEvents = myDatabase.daoAccess().getPersonForEvent(oldEventId);
                    if(personsForEvents.size() == 1){
                        myDatabase.daoAccess().deleteEventByEventId(oldEventId);
                    }
                     */
                }

                return null;

            }

            //todo:NEU
            protected void onPostExecute(Void result) {
                personsListener.onPostUpdatePerson();
            }
        }.execute();
    }





    // Geschenk hinzufügen Dialog

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

            //todo:NEU
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
            //todo: dann muss Mini-Acivity geöffnet werden --> (nach Speichern, gleiche Methode zur Abfrage nutzen?)
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

    //1. Teil: alte von zu upzudateten Present, 2. Teil: Werte aus EditText
    public void updatePresent(final String personFirstNameToUpdate, final String personLastNameToUpdate, final String eventToUpdate, final String presentNameToUpdate, final double priceToUpdate, final String shopToUpdate, final String statusToUpdate, final String firstName, final String lastName, final String eventName, final String presentName, final double presentPrice, final String presentShop, final String presentStatus){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int personId = myDatabase.daoAccess().getPersonIdByName(personFirstNameToUpdate, personLastNameToUpdate);
                Present present = getPresentByPresentInformation(personId, presentNameToUpdate,priceToUpdate,shopToUpdate,statusToUpdate);

                if((personFirstNameToUpdate != firstName) || (personLastNameToUpdate != lastName)){
                    insertPerson(firstName, lastName);
                    int newPersonId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);
                    present.setPersonId(newPersonId);
                }

                if(eventToUpdate != eventName){
                    int eventId;
                    if((personFirstNameToUpdate == firstName) && (personLastNameToUpdate == lastName)){
                        insertEventForPresent(personId, eventName);
                        eventId = getEventIdForPresent(personId, eventName);
                        insertPersonEventConnectionForPresent(personId, eventId);
                    }else{
                        int newPersonId = myDatabase.daoAccess().getPersonIdByName(firstName, lastName);
                        insertEventForPresent(newPersonId, eventName);
                        eventId = getEventIdForPresent(newPersonId, eventName);
                        insertPersonEventConnectionForPresent(newPersonId, eventId);
                    }
                    present.setEventId(eventId);
                }


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

            //todo:NEU
            protected void onPostExecute(Void result) {
                presentsListener.onPostUpdatePresent();
            }
        }.execute();
    }

    private Present getPresentByPresentInformation(int personId, String presentNameToUpdate, double priceToUpdateDouble, String shopToUpdate, String statusToUpdate){
        Present presentToUpdate;
        presentToUpdate = myDatabase.daoAccess().getPresentByPresentInformation(personId, presentNameToUpdate, priceToUpdateDouble, shopToUpdate, statusToUpdate);
        return presentToUpdate;
    }


    // Present Representation
    LiveData<List<PresentRepresentation>> getAllPresents() {
        return allPresents;
    }

    // Person Event Representation
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
