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
    List<String> getFirstNameByLAstName(String lastName);

    @Query("SELECT * FROM Person WHERE personId =:personId")
    List<Person> getPersonById(int personId);

    @Query("SELECT personId FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    int getPersonIdByName (String firstName, String lastName);

    @Delete
    void deletePerson(Person person);




    //Event Access

    @Insert
     void insertSingleEvent(Event event);

    @Insert
    void insertMoreEvents(Event... event);

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
    @Insert
    void insertPersonEventJoin(PersonEventJoin personEventJoin);

    //Present Access
    @Insert
    void insertPresent(Present present);
}
