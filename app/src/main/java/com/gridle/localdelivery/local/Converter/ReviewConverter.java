package com.gridle.localdelivery.local.Converter;

import androidx.room.TypeConverter;
import com.gridle.localdelivery.model.NearbyShopsResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class ReviewConverter {
    @TypeConverter
    public static List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> fromString(String value) {
        Type listType = new TypeToken<List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromArrayList(List<NearbyShopsResponse.Result.NearbyShopsObject.ReviewObject> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
