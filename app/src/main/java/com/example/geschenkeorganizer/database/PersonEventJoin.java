package com.example.geschenkeorganizer.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;

// https://developer.android.com/training/data-storage/room/relationships
@Entity(tableName = "person_event_join",
        primaryKeys = {"personId", "eventId"},
        foreignKeys = {@ForeignKey(entity=Person.class, parentColumns="personId", childColumns = "personId"), @ForeignKey(entity= Event.class, parentColumns = "eventId", childColumns = "eventId")})
public class PersonEventJoin {
    public int personId;
    public int eventId;
}
