package com.example.localdelivery.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.localdelivery.local.Dao.OrderDao;
import com.example.localdelivery.local.Dao.ShopsDao;
import com.example.localdelivery.local.Entity.OrderEntity;
import com.example.localdelivery.local.Entity.ShopsEntity;

@Database(entities = {ShopsEntity.class, OrderEntity.class}, version = 5, exportSchema = false)
public abstract class ShopsDatabase extends RoomDatabase {

    private static ShopsDatabase instance;

    public abstract ShopsDao shopsDao();

    public abstract OrderDao orderDao();

    public static synchronized ShopsDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context, ShopsDatabase.class, "shop_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
