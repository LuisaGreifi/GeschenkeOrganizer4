package com.example.geschenkeorganizer.database;

//todo: Neu
//Klasse, die als Rückgabewert für DAO-Abrfrage verwendet werden kann
//wird zur Darstellung der Geschenke in der App verwendet

//https://codelabs.developers.google.com/codelabs/android-persistence/#7
//Möglichkeit eigenes Objekt als Rückgabetyp für DAO-Abfrage

import androidx.annotation.NonNull;

public class PresentRepresentation {
    private String firstName;
    private String lastName;
    private String eventName;
    private String presentName;
    private double price;
    private String shop;
    private String status;

    public PresentRepresentation(){}

    //getter

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEventName(){
        return eventName;
    }

    public String getPresentName(){
        return presentName;
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

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setPresentName(@NonNull String presentName){
        this.presentName = presentName;
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

