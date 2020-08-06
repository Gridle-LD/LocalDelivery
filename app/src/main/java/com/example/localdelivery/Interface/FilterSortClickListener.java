package com.example.localdelivery.Interface;

public interface FilterSortClickListener {

    void setFilterClick(boolean isGrocerySelected, boolean isOthersSelected, boolean isDeliveryAvailableSelected);

    void setSortClick(boolean isRatingSelected, boolean isPopularitySelected, boolean isDistanceSelected);
}
