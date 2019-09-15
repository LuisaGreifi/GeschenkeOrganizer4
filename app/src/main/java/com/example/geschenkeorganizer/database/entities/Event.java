package com.example.geschenkeorganizer.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**Android Developers. (n.d.).
 * Defining data using Room entities. Index specific columns.
 * Retrieved from https://developer.android.com/training/data-storage/room/defining-data.
 * Verwendung Index und unique*/
@Entity(indices = {@Index(value = {"eventName", "eventDate"},
        unique = true)})
public class Event {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int eventId;

    private String eventName;
    private int eventDate;

    public Event(){}

    //getter
    @NonNull
    public int getEventId(){
        return eventId;
    }

    public String getEventName(){
        return eventName;
    }

    public int getEventDate(){
        return eventDate;
    }

    //setter
    public void setEventId(@NonNull int eventId){
        this.eventId = eventId;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setEventDate(int eventDate){
        this.eventDate = eventDate;
    }
}
