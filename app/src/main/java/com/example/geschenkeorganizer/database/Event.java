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

    // verschiedene Konstruktoren
    public Event(){}

    public Event(String eventName, String eventDate){}

    //getter
    public int getEventId(){
        return eventId;
    }

    public String getEventName(){
        return eventName;
    }

    public long getEventDate(){
        return eventDate;
    }

    //setter
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
