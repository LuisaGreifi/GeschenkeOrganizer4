package com.example.geschenkeorganizer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.geschenkeorganizer.database.access.DaoAccess;
import com.example.geschenkeorganizer.database.entities.Event;
import com.example.geschenkeorganizer.database.entities.Person;
import com.example.geschenkeorganizer.database.entities.PersonEventJoin;
import com.example.geschenkeorganizer.database.entities.Present;

@Database(entities={Person.class, Event.class, PersonEventJoin.class, Present.class}, version = 1, exportSchema=false)
public abstract class MyDatabase extends RoomDatabase {
        public abstract DaoAccess daoAccess();

        /**Google Developers Codelabs. (n.d.).
         * Android fundamentals 10.1 Part A: Room, LiveData, and ViewModel. Task 5: Add a Room database.
         * Retrieved from https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/#6.
         * Es kann nicht mehrere Datenbank Instanzen gleichzeitig geben */

        private static MyDatabase INSTANCE;

        public static MyDatabase getDatabase(final Context context) {
                if (INSTANCE == null) {
                        synchronized (MyDatabase.class) {
                                if (INSTANCE == null) {
                                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                                MyDatabase.class, "my_database")
                                                .fallbackToDestructiveMigration()
                                                .build();
                                }
                        }
                }
                return INSTANCE;
        }
}
