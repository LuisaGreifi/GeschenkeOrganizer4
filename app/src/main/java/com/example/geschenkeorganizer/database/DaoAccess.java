package com.example.geschenkeorganizer.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

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
    List<Person> getPersonById(int personId);

    @Query("SELECT personId FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    int getPersonIdByName (String firstName, String lastName);

    @Delete
    void deletePerson(Person person);





    //Event Access

    @Insert
     void insertSingleEvent(Event event);

    //vgl. Übung 6
    @Query("SELECT * FROM Event")
    List<Person> getAllEvents();

    // todo: Rückgabewert checken!!
    @Query("SELECT eventDate FROM Event")
    ArrayList<Long> getAllEventDates();

    @Query("SELECT eventName FROM Event")
    List<String> getAllEventNames();

    @Query("SELECT * FROM Event WHERE eventId =:eventId")
    List<Event> getEventById(int eventId);

    @Query("SELECT eventId FROM Event WHERE eventName =:eventName AND eventDate =:eventDate")
    int getEventIdByEventInformation (String eventName, long eventDate);

    @Delete
    void deleteEvent(Event event);





    //PersonEventJoin Access

    //vgl. https://developer.android.com/training/data-storage/room/relationships
    @Insert
    void insertPersonEventJoin(PersonEventJoin personEventJoin);

    //vgl. https://developer.android.com/training/data-storage/room/relationships
    @Query("SELECT * FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId")
    List<Event> getEventForPerson(final int personId);

    @Query("SELECT eventName FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId")
    List<String> getEventNameForPerson(final int personId);

    //todo: absolut nicht sicher, ob das so funktioniert!
    @Query("SELECT eventName FROM Event, Person " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE Person.firstName =:firstName")
    List<String> getEventNameForPersonsFirstName(final String firstName);

    //vgl. https://developer.android.com/training/data-storage/room/relationships
    @Query("SELECT * FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<Person> getPersonForEvent(final int eventId);

    @Query("SELECT firstName FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<String> getPersonsFirstNameForEvent(final int eventId);

    @Query("SELECT lastName FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<String> getPersonsLastNameForEvent(final int eventId);

    @Delete
    void deletePersonEventJoin(PersonEventJoin presentEventJoin);





    //Present Access

    @Insert
    void insertPresent(Present present);

    //vgl. Übung 6
    @Query("SELECT * FROM Present")
    List<Present> getAllPresents();

    @Query("SELECT * FROM Present WHERE presentId =:presentId")
    List<Present> getPresentById(int presentId);

    @Query("SELECT * FROM Present WHERE personId =:personId")
    List<Present> getPresentByPersonId(int personId);

    @Query("SELECT * FROM Present WHERE eventId =:eventId")
    List<Present> getPresentByEventId(int eventId);

    @Delete
    void deletePresent(Present present);



    //todo: absolut unsicher --> ÜBERPRÜFEN!
    //Present Representation
    @Query("SELECT Person.firstName, Person.lastName, Event.eventName, Present.presentTitle, Present.price, Present.shop, Present.status FROM Person, Event, Present WHERE Present.personId = Person.personId AND Present.eventId = Event.eventId")
    List<String> getPresentRepresentataion();
    // brauch noch ein eigenes Objekt dafür :)

    //Alternativ vllt:
    //Element 1: Name Person zu personId in Geschenke bekommen
    // Inspiration: //vgl. https://developer.android.com/training/data-storage/room/relationships
    @Query("SELECT firstName, lastName FROM Person " + "INNER JOIN Present " + "ON Person.personId = Present.personId " + "WHERE Present.personId =:presentPersonId")
    List<Person> getPersonNameForPresentPersonId(final int presentPersonId);
    //Element 2: Eventtitel zu Event ID in Geschenk bekommen
    // Inspiration: //vgl. https://developer.android.com/training/data-storage/room/relationships
    @Query("SELECT eventName FROM Event " + "INNER JOIN Present " + "ON Event.eventId = Present.eventId " + "WHERE Present.eventId =:presentEventId")
    List<String> getEventNameForPresentEventId(final int presentEventId);
    //Element 3:
    @Query("SELECT presentTitle, price, shop, status FROM Present")
    List<Present> getPresentRepresentationInformation();
    
    //dann resultiert ungefähr sowas
    @Query("SELECT Person.firstName, Person.lastName, Event.eventName, Present.presentTitle, Present.price, Present.shop, Present.status FROM Person, Event, Present " + "INNER JOIN Present " + "ON Person.personId = Present.personId AND Event.eventId = Present.eventId")
    List<Present> getAllPresentsForRepresentation();
    //brauchst eigenes Objekt zur Anzeige! --> Pojo
}
