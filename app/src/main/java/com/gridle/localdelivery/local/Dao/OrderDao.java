package com.gridle.localdelivery.local.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.gridle.localdelivery.local.Entity.OrderEntity;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<OrderEntity> orderEntities);

    @Update
    void update(OrderEntity orderEntity);

    @Delete
    void delete(OrderEntity orderEntity);

    @Query("delete from order_table")
    void deleteAll();

    @Query("select * from order_table")
    LiveData<List<OrderEntity>> getAllOrders();
}
