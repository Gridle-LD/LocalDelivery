package com.example.localdelivery.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.localdelivery.local.Entity.ShopsEntity;
import java.util.List;

@Dao
public interface ShopsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ShopsEntity> shopsEntities);

    @Update
    void update(ShopsEntity shopsEntity);

    @Delete
    void delete(ShopsEntity shopsEntity);

    @Query("delete from shops_table")
    void deleteAll();

    @Query("select * from shops_table")
    LiveData<List<ShopsEntity>> getAllShops();
}
