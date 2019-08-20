package com.example.geschenkeorganizer.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={Person.class, Event.class, PersonEventJoin.class, Present.class}, version = 1, exportSchema=false)
public abstract class MyDatabase extends RoomDatabase {
        public abstract DaoAccess daoAccess();
}
