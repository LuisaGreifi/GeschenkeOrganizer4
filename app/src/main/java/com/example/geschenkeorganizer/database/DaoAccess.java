package com.example.geschenkeorganizer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DaoAccess {
    // Person Access

    @Insert
    void insertPerson(Person person);

    //vgl. Übung 6
    @Query("SELECT * FROM Person")
    List<Person> getAllPersons();

    @Query("SELECT firstName FROM Person")
    List<String> getAllFirstNames();

    @Query("SELECT lastName FROM Person")
    List<String> getAllLastNames();

    //vgl. Übung 6
    @Query("SELECT lastName FROM Person WHERE firstName =:firstName")
    List<String> getLastNameByFirstName(String firstName);

    //vgl. Übung 6
    @Query("SELECT firstName FROM Person WHERE lastName =:lastName")
    List<String> getFirstNameByLastName(String lastName);

    @Query("SELECT * FROM Person WHERE personId =:personId")
    Person getPersonById(int personId);

    @Query("SELECT personId FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    int getPersonIdByName(String firstName, String lastName);

    @Query("SELECT * FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    boolean existsPersonWithNameAlready(String firstName, String lastName);

    // https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3
    @Query("DELETE FROM Person")
    void deleteAllPersons();

    @Query("DELETE FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    void deletePersonByName(String firstName, String lastName);


    //Event Access

    @Insert
    void insertEvent(Event event);

    //unktioniert für mehrere (und für eins!)
    //Unterscheidung bei mehreren komplizierter, ob schon in DB oder nicht...(nur wenn unbedingt nötig für UI!)
    @Insert
    void insertEvents(Event... event);

    //vgl. Übung 6
    @Query("SELECT * FROM Event")
    List<Event> getAllEvents();

    @Query("SELECT eventDate FROM Event")
    List<Integer> getAllEventDates();

    @Query("SELECT eventName FROM Event")
    List<String> getAllEventNames();

    @Query("SELECT * FROM Event WHERE eventId =:eventId")
    Event getEventById(int eventId);

    @Query("SELECT eventId FROM Event WHERE eventName =:eventName AND eventDate =:eventDate")
    int getEventIdByEventInformation(String eventName, int eventDate);

    @Query("SELECT * FROM Event WHERE eventName =:eventName AND eventDate =:eventDate")
    boolean existsEventWithEventInformationAlready(String eventName, int eventDate);

    // https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3
    @Query("DELETE FROM Event")
    void deleteAllEvents();

    @Query("DELETE FROM Event WHERE eventName =:eventName AND eventDate =:eventDate")
    void deleteEventByEventInformation(String eventName, int eventDate);

    @Query("DELETE FROM Event WHERE eventId =:eventId")
    void deleteEventByEventId(int eventId);


    //PersonEventJoin Access

    //vgl. https://developer.android.com/training/data-storage/room/relationships
    @Insert
    void insertPersonEventJoin(PersonEventJoin personEventJoin);

    //vgl. Übung 6
    @Query("SELECT * FROM PersonEventJoin")
    List<PersonEventJoin> getAllPersonEventJoins();

    //vgl. https://developer.android.com/training/data-storage/room/relationships
    // könnte gut für Spinner in Geschenke hinzufügen sein
    // auch: wahrscheinlich Nutzung zur Anzeige Events passend zu den Personen
    @Query("SELECT * FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId")
    List<Event> getEventForPerson(final int personId);

    //eher das gut für Spinner
    @Query("SELECT eventName FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId")
    List<String> getEventNameForPerson(final int personId);

    @Query("SELECT eventName FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId AND Event.eventDate =:eventDate")
    String getEventNameForPersonAndEventDate(final int personId, final int eventDate);

    @Query("SELECT eventDate FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId AND Event.eventName =:eventName")
    int getEventDateForPersonAndEventName(final int personId, final String eventName);

    @Query("SELECT eventDate FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId AND Event.eventId =:eventId")
    int getEventDateForPersonAndEvent(final int personId, final int eventId);

    //Person-Event-Join zu Event (& Event) dürfen halt nicht existieren
    @Query("SELECT * FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE personId =:personId AND eventName =:eventName")
    boolean existsEventForPersonAlready(int personId, String eventName);

    //vgl. https://developer.android.com/training/data-storage/room/relationships
    @Query("SELECT * FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<Person> getPersonForEvent(final int eventId);

    @Query("SELECT firstName FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<String> getPersonsFirstNameForEvent(final int eventId);

    @Query("SELECT lastName FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<String> getPersonsLastNameForEvent(final int eventId);

    @Query("SELECT * FROM PersonEventJoin WHERE personId =:personId AND eventId =:eventId")
    boolean existsPersonEventConnectionAlready(int personId, int eventId);

    // https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3
    @Query("DELETE FROM PersonEventJoin")
    void deleteAllPersonEventJoins();

    @Query("DELETE FROM PersonEventJoin WHERE personId =:personId AND eventId =:eventId")
    void deletePersonEventJoin(int personId, int eventId);


    //Present Access

    @Insert
    void insertPresent(Present present);

    //vgl. Übung 6
    @Query("SELECT * FROM Present")
    List<Present> getAllPresents();

    @Query("SELECT * FROM Present WHERE presentId =:presentId")
    Present getPresentById(int presentId);

    @Query("SELECT * FROM Present WHERE personId =:personId")
    List<Present> getPresentsByPersonId(int personId);

    @Query("SELECT * FROM Present WHERE eventId =:eventId")
    List<Present> getPresentsByEventId(int eventId);

    @Query("SELECT * FROM Present WHERE eventId =:eventId")
    boolean existsPresentForEventAlready(int eventId);

    // https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3
    @Query("DELETE FROM Present")
    void deleteAllPresents();

    @Query("DELETE FROM Present WHERE personId =:personId AND eventId =:eventId")
    void deletePresentByPersonAndEvent(int personId, int eventId);

    @Query("DELETE FROM Present WHERE personId =:personId")
    void deletePresentByPerson(int personId);

    @Query("DELETE FROM Present WHERE eventId =:eventId")
    void deletePresentByEvent(int eventId);


    //Present Representation

    //todo: NEU

    //https://codelabs.developers.google.com/codelabs/android-persistence/#7
    // Möglichkeit eigenes Objekt als Rückgabetyp für DAO-Abfrage
    // https://wiki.selfhtml.org/wiki/Datenbank/Fortgeschrittene_Jointechniken#Unterschiedliche_Join-Spalten_nutzen
    // inner join + Struktur
    // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#5
    // LiveData als Rückgabe
    @Query("SELECT firstName, lastName, eventName, presentName, price, shop, status FROM Present " + "INNER JOIN Person INNER JOIN Event " + "ON Present.eventId = Event.eventId AND Present.personId = Person.personId")
    LiveData<List<PresentRepresentation>> getAllPresentsForRepresentation();


// Person Event Representation

//todo: NEU

    //https://codelabs.developers.google.com/codelabs/android-persistence/#7
    // Möglichkeit eigenes Objekt als Rückgabetyp für DAO-Abfrage
    // https://wiki.selfhtml.org/wiki/Datenbank/Fortgeschrittene_Jointechniken#Unterschiedliche_Join-Spalten_nutzen
    // inner join + Struktur
    // https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#5
    // LiveData als Rückgabe
    @Query("SELECT firstName, lastName, eventName, eventDate FROM PersonEventJoin " + "INNER JOIN Person INNER JOIN Event " + "ON PersonEventJoin.eventId = Event.eventId AND PersonEventJoin.personId = Person.personId")
    LiveData<List<PersonEventRepresentation>> getAllPersonsWithEventsForRepresentation();

}