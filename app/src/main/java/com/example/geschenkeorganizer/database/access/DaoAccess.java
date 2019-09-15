package com.example.geschenkeorganizer.database.access;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.geschenkeorganizer.database.entities.Event;
import com.example.geschenkeorganizer.database.entities.Person;
import com.example.geschenkeorganizer.database.entities.PersonEventJoin;
import com.example.geschenkeorganizer.database.representations.PersonEventRepresentation;
import com.example.geschenkeorganizer.database.entities.Present;
import com.example.geschenkeorganizer.database.representations.PresentRepresentation;

import java.util.List;


@Dao
public interface DaoAccess {

    // Person Access

    @Insert
    void insertPerson(Person person);

    /**vgl. Schwappach F. & Jelinski J. (25.05.2019).
    * Übungsaufgabe 6. Telefonbuch-Datenbank [Vorlesungsfolien].
    * Retrieved from https://ilias.uni-passau.de/ilias/ilias.php?ref_id=89588&cmd=view&cmdClass=ilrepositorygui&cmdNode=t0&baseClass=ilRepositoryGUI. */
    @Query("SELECT * FROM Person")
    List<Person> getAllPersons();

    @Query("SELECT firstName FROM Person")
    List<String> getAllFirstNames();

    @Query("SELECT lastName FROM Person")
    List<String> getAllLastNames();

    /**vgl. Schwappach F. & Jelinski J. (25.05.2019).
     * Übungsaufgabe 6. Telefonbuch-Datenbank [Vorlesungsfolien].
     * Retrieved from https://ilias.uni-passau.de/ilias/ilias.php?ref_id=89588&cmd=view&cmdClass=ilrepositorygui&cmdNode=t0&baseClass=ilRepositoryGUI. */
    @Query("SELECT lastName FROM Person WHERE firstName =:firstName")
    List<String> getLastNameByFirstName(String firstName);

    /**vgl. Schwappach F. & Jelinski J. (25.05.2019).
     * Übungsaufgabe 6. Telefonbuch-Datenbank [Vorlesungsfolien].
     * Retrieved from https://ilias.uni-passau.de/ilias/ilias.php?ref_id=89588&cmd=view&cmdClass=ilrepositorygui&cmdNode=t0&baseClass=ilRepositoryGUI. */
    @Query("SELECT firstName FROM Person WHERE lastName =:lastName")
    List<String> getFirstNameByLastName(String lastName);

    @Query("SELECT * FROM Person WHERE personId =:personId")
    Person getPersonById(int personId);

    @Query("SELECT personId FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    int getPersonIdByName(String firstName, String lastName);

    @Query("SELECT * FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    boolean existsPersonWithNameAlready(String firstName, String lastName);

    /**Google Developers Codelabs. (n.d.).
     * Android fundamentals 10.1 Part B: Deleting data from a Room database. 4. Task 2: Delete all words.
     * Retrieved from https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3.
     * Funktionsweise Eintrag löschen */
    @Query("DELETE FROM Person")
    void deleteAllPersons();

    @Query("DELETE FROM Person WHERE firstName =:firstName AND lastName =:lastName")
    void deletePersonByName(String firstName, String lastName);





    //Event Access

    @Insert
    void insertEvent(Event event);

    /**vgl. Schwappach F. & Jelinski J. (25.05.2019).
     * Übungsaufgabe 6. Telefonbuch-Datenbank [Vorlesungsfolien].
     * Retrieved from https://ilias.uni-passau.de/ilias/ilias.php?ref_id=89588&cmd=view&cmdClass=ilrepositorygui&cmdNode=t0&baseClass=ilRepositoryGUI. */
    @Query("SELECT * FROM Event")
    List<Event> getAllEvents();

    @Query("SELECT eventDate FROM Event")
    List<Integer> getAllEventDates();

    @Query("SELECT eventName FROM Event")
    List<String> getAllEventNames();

    @Query("SELECT * FROM Event WHERE eventId =:eventId")
    Event getEventById(int eventId);

    @Query("SELECT eventId FROM Event WHERE eventName =:eventName AND eventDate =:eventDate")
    int getEventIdByEventInformation(String eventName, int eventDate);

    @Query("SELECT * FROM Event WHERE eventName =:eventName AND eventDate =:eventDate")
    boolean existsEventWithEventInformationAlready(String eventName, int eventDate);

    /**Google Developers Codelabs. (n.d.).
     * Android fundamentals 10.1 Part B: Deleting data from a Room database. 4. Task 2: Delete all words.
     * Retrieved from https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3.
     * Funktionsweise Eintrag löschen */
    @Query("DELETE FROM Event")
    void deleteAllEvents();

    @Query("DELETE FROM Event WHERE eventName =:eventName AND eventDate =:eventDate")
    void deleteEventByEventInformation(String eventName, int eventDate);

    @Query("DELETE FROM Event WHERE eventId =:eventId")
    void deleteEventByEventId(int eventId);





    //PersonEventJoin Access

    /*Android Developers. (n.d.).
    * Define relationships between objects.Define many-to-many relationships.
    * Retrieved from https://developer.android.com/training/data-storage/room/relationships.
    * Erstellung many-to-many relationship*/
    @Insert
    void insertPersonEventJoin(PersonEventJoin personEventJoin);

    /**vgl. Schwappach F. & Jelinski J. (25.05.2019).
     * Übungsaufgabe 6. Telefonbuch-Datenbank [Vorlesungsfolien].
     * Retrieved from https://ilias.uni-passau.de/ilias/ilias.php?ref_id=89588&cmd=view&cmdClass=ilrepositorygui&cmdNode=t0&baseClass=ilRepositoryGUI. */
    @Query("SELECT * FROM PersonEventJoin")
    List<PersonEventJoin> getAllPersonEventJoins();

    /**Android Developers. (n.d.).
    * Define relationships between objects.Define many-to-many relationships.
    * Retrieved from https://developer.android.com/training/data-storage/room/relationships.
    * Funktionsweise für Abfragen von beiden Tabellen*/
    @Query("SELECT * FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId")
    List<Event> getEventForPerson(final int personId);

    @Query("SELECT eventName FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId")
    List<String> getEventNameForPerson(final int personId);

    @Query("SELECT eventName FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId AND Event.eventDate =:eventDate")
    String getEventNameForPersonAndEventDate(final int personId, final int eventDate);

    @Query("SELECT eventDate FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId AND Event.eventName =:eventName")
    int getEventDateForPersonAndEventName(final int personId, final String eventName);

    @Query("SELECT eventDate FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE PersonEventJoin.personId =:personId AND Event.eventId =:eventId")
    int getEventDateForPersonAndEvent(final int personId, final int eventId);

    @Query("SELECT * FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<Person> getPersonForEvent(final int eventId);

    @Query("SELECT firstName FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<String> getPersonsFirstNameForEvent(final int eventId);

    @Query("SELECT lastName FROM Person " + "INNER JOIN PersonEventJoin " + "ON Person.personId = PersonEventJoin.personId " + "WHERE PersonEventJoin.eventId =:eventId")
    List<String> getPersonsLastNameForEvent(final int eventId);

    @Query("SELECT * FROM Event " + "INNER JOIN PersonEventJoin " + "ON Event.eventId = PersonEventJoin.eventId " + "WHERE personId =:personId AND eventName =:eventName")
    boolean existsEventForPersonAlready(int personId, String eventName);

    @Query("SELECT * FROM PersonEventJoin WHERE personId =:personId AND eventId =:eventId")
    boolean existsPersonEventConnectionAlready(int personId, int eventId);

    /**Google Developers Codelabs. (n.d.).
     * Android fundamentals 10.1 Part B: Deleting data from a Room database. 4. Task 2: Delete all words.
     * Retrieved from https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3.
     * Funktionsweise Eintrag löschen */
    @Query("DELETE FROM PersonEventJoin")
    void deleteAllPersonEventJoins();

    //todo: neu
    @Query("DELETE FROM PersonEventJoin WHERE personId =:personId AND eventId =:eventId")
    void deletePersonEventJoin(int personId, int eventId);





    //Present Access

    @Insert
    void insertPresent(Present present);

    @Update
    void updatePresent(Present present);

    /**vgl. Schwappach F. & Jelinski J. (25.05.2019).
     * Übungsaufgabe 6. Telefonbuch-Datenbank [Vorlesungsfolien].
     * Retrieved from https://ilias.uni-passau.de/ilias/ilias.php?ref_id=89588&cmd=view&cmdClass=ilrepositorygui&cmdNode=t0&baseClass=ilRepositoryGUI. */
    @Query("SELECT * FROM Present")
    List<Present> getAllPresents();

    @Query("SELECT * FROM Present WHERE presentId =:presentId")
    Present getPresentById(int presentId);

    @Query("SELECT * FROM Present WHERE personId =:personId")
    List<Present> getPresentsByPersonId(int personId);

    @Query("SELECT * FROM Present WHERE eventId =:eventId")
    List<Present> getPresentsByEventId(int eventId);

    @Query("SELECT * FROM Present WHERE personId =:personId AND presentName =:presentName AND price =:price AND shop =:shop AND status =:status")
    Present getPresentByPresentInformation(int personId, String presentName, double price, String shop, String status);

    @Query("SELECT * FROM Present WHERE eventId =:eventId")
    boolean existsPresentForEventAlready(int eventId);

    /**Google Developers Codelabs. (n.d.).
     * Android fundamentals 10.1 Part B: Deleting data from a Room database. 4. Task 2: Delete all words.
     * Retrieved from https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html#3.
     * Funktionsweise Eintrag löschen */
    @Query("DELETE FROM Present")
    void deleteAllPresents();

    @Query("DELETE FROM Present WHERE personId =:personId AND eventId =:eventId")
    void deletePresentByPersonAndEvent(int personId, int eventId);

    @Query("DELETE FROM Present WHERE personId =:personId")
    void deletePresentByPerson(int personId);

    @Query("DELETE FROM Present WHERE eventId =:eventId")
    void deletePresentByEvent(int eventId);





    // Person Event Representation

    /**Google Developers Codelabs. (n.d.).
     * Create the ViewModel. Implement the ViewModel.
     * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#5
     * Möglichkeit LiveData als Rückgabe zu verwenden */
    /**
     /**Google Developers Codelabs. (n.d.).
     * Step 5 - Add Custom Query Result Objects.
     * Retrieved from: https://codelabs.developers.google.com/codelabs/android-persistence/#7.
     * Möglichkeit eigenes Objekt als Rückgabetyp für DAO-Abfrage zu verwenden */
    /**
     * Mehrere Tabellen mit JOIN verknüpfen - unterschiedliche Joinspalten. Unterschiedliche Join-Spalten nutzen. (n.d.).
     * Retrieved from: https://wiki.selfhtml.org/wiki/Datenbank/Fortgeschrittene_Jointechniken#Unterschiedliche_Join-Spalten_nutzen
     * Struktur Abfrage und Funktionsweise INNER JOIN*/
    @Query("SELECT firstName, lastName, eventName, eventDate FROM PersonEventJoin " + "INNER JOIN Person INNER JOIN Event " + "ON PersonEventJoin.eventId = Event.eventId AND PersonEventJoin.personId = Person.personId " + "ORDER BY firstName, lastName")
    LiveData<List<PersonEventRepresentation>> getAllPersonsWithEventsForRepresentation();





    //Present Representation

    /**Google Developers Codelabs. (n.d.).
     * Create the ViewModel. Implement the ViewModel.
     * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#5
     * Möglichkeit LiveData als Rückgabe zu verwenden */
    /**
    /**Google Developers Codelabs. (n.d.).
     * Step 5 - Add Custom Query Result Objects.
     * Retrieved from: https://codelabs.developers.google.com/codelabs/android-persistence/#7.
     * Möglichkeit eigenes Objekt als Rückgabetyp für DAO-Abfrage zu verwenden */
    /**
     * Mehrere Tabellen mit JOIN verknüpfen - unterschiedliche Joinspalten. Unterschiedliche Join-Spalten nutzen. (n.d.).
     * Retrieved from: https://wiki.selfhtml.org/wiki/Datenbank/Fortgeschrittene_Jointechniken#Unterschiedliche_Join-Spalten_nutzen
     * Struktur Abfrage und Funktionsweise INNER JOIN*/
    @Query("SELECT firstName, lastName, eventName, presentName, price, shop, status FROM Present " + "INNER JOIN Person INNER JOIN Event " + "ON Present.eventId = Event.eventId AND Present.personId = Person.personId")
    LiveData<List<PresentRepresentation>> getAllPresentsForRepresentation();
}