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
    public int getPresentId(){
        return presentId;
    }

    public String getPresentTitle(){
        return presentTitle;
    }

    public double getPresentPrice(){
        return price;
    }

    public String getPresentShop(){
        return shop;
    }

    public String getPresentStatus(){
        return status;
    }

    //setter
    public void setpresentId(@NonNull int presentId){
        this.presentId = presentId;
    }

    public void setPresentTitle(@NonNull String presentTitle){
        this.presentTitle = presentTitle;
    }

    public void setPresentPrice(double price){
        this.price = price;
    }

    public void setPresentShop(String shop){
        this.shop = shop;
    }

    public void setPresentStatus(String status){
        this.status = status;
    }

}
