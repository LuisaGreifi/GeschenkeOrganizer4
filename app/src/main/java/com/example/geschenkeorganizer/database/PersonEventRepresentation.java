package com.example.geschenkeorganizer.database;

/**Klasse, die als Rückgabewert für DAO-Abrfrage verwendet werden kann
 * wird zur Darstellung der Personen und Events in der App verwendet. */


/**Google Developers Codelabs. (n.d.).
 * Step 5 - Add Custom Query Result Objects.
 * Retrieved from https://codelabs.developers.google.com/codelabs/android-persistence/#7.
 * Möglichkeit eigenes Objekt als Rückgabetyp für DAO-Abfrage zu verwenden */

public class PersonEventRepresentation {
    private String firstName;
    private String lastName;
    private String eventName;
    private int eventDate;

    public PersonEventRepresentation(){}

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

    public int getEventDate(){
        return eventDate;
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

    public void setEventDate(int eventDate){
        this.eventDate = eventDate;
    }

}
