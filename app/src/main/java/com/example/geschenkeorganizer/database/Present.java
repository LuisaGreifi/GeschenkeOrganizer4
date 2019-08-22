package com.example.geschenkeorganizer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/** https://developer.android.com/training/data-storage/room/relationships
 * one-to-many relationship */
@Entity(foreignKeys = {@ForeignKey(entity = Person.class, parentColumns = "personId", childColumns = "personId"), @ForeignKey(entity = Event.class, parentColumns = "eventId", childColumns = "eventId")})
public class Present {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int presentId;

    @NonNull
    public int personId;
    public int eventId;

    @NonNull
    private String presentTitle;
    private double price;
    private String shop;
    private String status;

    // verschiedene Konstruktoren
    public Present(){}

    public Present(String presentTitle){}

    public Present(String presentTitle, String status){}

    public Present(String presentTitle, double price, String shop, String status){}

    //getter
    @NonNull
    public int getPresentId(){
        return presentId;
    }

    @NonNull
    public int getPersonId(){
        return personId;
    }

    public int getEventId(){
        return eventId;
    }

    @NonNull
    public String getPresentTitle(){
        return presentTitle;
    }

    public double getPrice(){
        return price;
    }

    public String getShop(){
        return shop;
    }

    public String getStatus(){
        return status;
    }

    //setter
    public void setPresentId(@NonNull int presentId){
        this.presentId = presentId;
    }

    public void setPersonId(@NonNull int personId){
        this.personId = personId;
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
    }

    public void setPresentTitle(@NonNull String presentTitle){
        this.presentTitle = presentTitle;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setShop(String shop){
        this.shop = shop;
    }

    public void setStatus(String status){
        this.status = status;
    }

}
