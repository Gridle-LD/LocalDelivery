package com.example.localdelivery.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShopsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ShopsEntity> shopsEntities);

    @Update
    void update(ShopsEntity shopsEntity);

    @Delete
    void delete(ShopsEntity shopsEntity);

    @Query("select * from shops_table")
    LiveData<List<ShopsEntity>> getAllShops();
}