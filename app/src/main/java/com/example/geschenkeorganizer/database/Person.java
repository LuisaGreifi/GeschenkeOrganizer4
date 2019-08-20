package com.example.geschenkeorganizer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Person {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int personId;

    private String firstName;
    private String lastName;

    public Person(){}

    // oder in DAO-Methode
    public Person(String firstName, String lastName){}

    public int getPersonId(){
        return personId;
    }


    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

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
