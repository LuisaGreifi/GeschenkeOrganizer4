package com.example.geschenkeorganizer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/** https://developer.android.com/training/data-storage/room/relationships
many-to-many relationship --> grundlegender Aufbau Join Klasse*/
@Entity(primaryKeys = {"personId", "eventId"},
        foreignKeys = {@ForeignKey(entity=Person.class, parentColumns="personId", childColumns = "personId"), @ForeignKey(entity= Event.class, parentColumns = "eventId", childColumns = "eventId")})
public class PersonEventJoin {
    // todo: bei android developers public
    private int personId;
    private int eventId;

    public PersonEventJoin(){}

    public PersonEventJoin(int personId, int eventId){}

    public int getPersonId(){
        return personId;
    }

    public int getEventId(){
        return eventId;
    }

    public void setPersonId(int personId){
        this.personId = personId;
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
    }
}
