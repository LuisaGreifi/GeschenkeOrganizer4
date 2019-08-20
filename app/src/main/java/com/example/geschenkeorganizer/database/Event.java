package com.example.geschenkeorganizer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int eventId;

    private String eventName;
    private long eventDate;

    public Event(){}

    // oder in DAO-Methode
    public Event(String eventName, String eventDate){}

    public int getEventId(){
        return eventId;
    }

    public String getEventName(){
        return eventName;
    }

    public long getEventDate(){
        return eventDate;
    }

    public void setEventId(@NonNull int eventId){
        this.eventId = eventId;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setEventDate(Long eventDate){
        this.eventDate = eventDate;
    }
}
