package com.example.geschenkeorganizer.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

/**Android Developers. (n.d.).
 * Define relationships between objects. Define many-to-many relationships.
 * Retrieved from https://developer.android.com/training/data-storage/room/relationships.
 * Erstellung many-to-many relationship (grundlegender Aufbau der Join-Klasse)*/

@Entity(primaryKeys = {"personId", "eventId"},
        foreignKeys = {@ForeignKey(entity=Person.class, parentColumns="personId", childColumns = "personId"), @ForeignKey(entity= Event.class, parentColumns = "eventId", childColumns = "eventId")})
public class PersonEventJoin {
    // todo: bei android developers public
    private int personId;
    private int eventId;

    public PersonEventJoin(){}

    public PersonEventJoin(int personId, int eventId){}

    //getter
    public int getPersonId(){
        return personId;
    }

    public int getEventId(){
        return eventId;
    }

    // setter
    public void setPersonId(int personId){
        this.personId = personId;
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
    }
}
