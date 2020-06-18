package com.example.localdelivery.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = ShopsEntity.class, version = 1, exportSchema = false)
//@TypeConverters({Converter.class})
public abstract class ShopsDatabase extends RoomDatabase {

    private static ShopsDatabase instance;

    public abstract ShopsDao shopsDao();

    public static synchronized ShopsDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context, ShopsDatabase.class, "shop_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
