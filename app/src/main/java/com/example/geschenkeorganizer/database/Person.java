package com.example.geschenkeorganizer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** https://developer.android.com/training/data-storage/room/defining-data
 Index und unique */
@Entity(indices = {@Index(value = {"firstName", "lastName"},
        unique = true)})
public class Person {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int personId;

    private String firstName;
    private String lastName;

    // verschiedene Konstruktoren
    public Person(){}

    public Person(String firstName, String lastName){}

    //getter
    @NonNull
    public int getPersonId(){
        return personId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    //setter
    public void setPersonId(@NonNull int personId){
        this.personId = personId;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }
}
