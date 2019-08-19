package com.example.geschenkeorganizer;

import java.util.ArrayList;
import java.util.Date;

public class Person {

    //Variablen und Konstanten

    private String firstName;
    private String surName;
    private Date birthDate;
    private String event;
    private ArrayList<String> events;

    //Verschiedene Konstruktoren

    public Person(String firstName, String surName) {
        this.firstName = firstName;
        this.surName = surName;
    }

    public Person(String firstName, String surName, Date birthDate) {
        this.firstName = firstName;
        this.surName = surName;
        this.birthDate = birthDate;
    }

    public Person(String firstName, String surName, Date birthDate, String event) {
        this.firstName = firstName;
        this.surName = surName;
        this.birthDate = birthDate;
        this.event = event;
        events.add(event);
    }


    public Person(String firstName, String surName, String event) {
        this.firstName = firstName;
        this.surName = surName;
        this.event = event;
        events.add(event);
    }

    //getter-Methoden

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getEvent() {
        return event;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    //setter-Methoden

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private void setSurName(String surName) {
        this.surName = surName;
    }

    private void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    private void setEvent(String event) {
        this.event = event;
    }

    private void setEvents(ArrayList<String> events) {
        this.events = events;
    }
}
