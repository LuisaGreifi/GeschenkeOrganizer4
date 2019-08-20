package com.example.geschenkeorganizer.database;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface DaoAccess {
    // Person Access
     @Insert
     void insertPerson(Person person);

     //aus der Übung 6
     @Query("SELECT * FROM Person")
    List<Person> getAll();

    //Event Access
    @Insert
     void insertEvent(Event event);

    //PersonEventJoin Access
    @Insert
    void insertPersonEventJoin(PersonEventJoin personEventJoin);

    //Present Access
    @Insert
    void insertPresent(Present present);
}
