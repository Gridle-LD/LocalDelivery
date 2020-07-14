package com.example.localdelivery.local;

import androidx.room.TypeConverter;
import com.example.localdelivery.model.StocksData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class StocksConverter {
    @TypeConverter
    public static List<StocksData> fromString(String value) {
        Type listType = new TypeToken<List<StocksData>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayList(List<StocksData> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}