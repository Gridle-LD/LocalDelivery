package com.gridle.localdelivery.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.gridle.localdelivery.local.Dao.OrderDao;
import com.gridle.localdelivery.local.Dao.ShopsDao;
import com.gridle.localdelivery.local.Entity.OrderEntity;
import com.gridle.localdelivery.local.Entity.ShopsEntity;

@Database(entities = {ShopsEntity.class, OrderEntity.class}, version = 7, exportSchema = false)
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
