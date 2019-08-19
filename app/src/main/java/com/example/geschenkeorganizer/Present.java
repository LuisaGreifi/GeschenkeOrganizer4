package com.example.geschenkeorganizer;

public class Present {

    //Variablen und Konstanten

    private String firstName, surName, description, placeOfPurchase, event;
    private boolean hadIdea, bought, wrapped;
    private double price;
    private int id;


    //Konstruktor

    public Present(String firstName, String surname, String description, String placeOfPurchase, String event, boolean hadIdea, boolean bought, boolean wrapped, double price) {
        this.firstName = firstName;
        this.surName = surname;
        this.description = description;
        this.placeOfPurchase = placeOfPurchase;
        this.event = event;
        this.hadIdea = hadIdea;
        this.bought = bought;
        this.wrapped = wrapped;
        this.price = price;
    }

    //getter-Methoden

    public String getFirstName() {
        return firstName;
    }
    public String getSurname() {
        return surName;
    }
    public String getDescription() {
        return description;
    }
    public String getPlaceOfPurchase() {
        return placeOfPurchase;
    }
    public String getEvent() {
        return event;
    }
    public boolean isHadIdea() {
        return hadIdea;
    }
    public boolean isBought() {
        return bought;
    }
    public boolean isWrapped() {
        return wrapped;
    }
    public double getPrice() {
        return price;
    }
    public int getId() {
        return id;
    }

    //setter-Methoden

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setSurname(String surName) {
        this.surName = surName;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPlaceOfPurchase(String placeOfPurchase) {
        this.placeOfPurchase = placeOfPurchase;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public void setHadIdea(boolean hadIdea) {
        this.hadIdea = hadIdea;
    }
    public void setBought(boolean bought) {
        this.bought = bought;
    }
    public void setWrapped(boolean wrapped) {
        this.wrapped = wrapped;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return firstName + surName + ": " + description;
    }
}
