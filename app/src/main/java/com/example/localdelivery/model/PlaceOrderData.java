package com.example.localdelivery.model;

import java.util.List;

public class PlaceOrderData {

    private List<Order> order;

    public PlaceOrderData(List<Order> order) {
        this.order = order;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public static class Order {
        private Shop shop;

        public Order(Shop shop) {
            this.shop = shop;
        }

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }

        public static class Shop {
            private String shopId;
            private List<Items> items;

            public Shop(String shopId, List<Items> items) {
                this.shopId = shopId;
                this.items = items;
            }

            public String getShopId() {
                return shopId;
            }

            public void setShopId(String shopId) {
                this.shopId = shopId;
            }

            public List<Items> getItems() {
                return items;
            }

            public void setItems(List<Items> items) {
                this.items = items;
            }

            public static class Items {
                private String quantity;
                private String item;

                public Items(String quantity, String item) {
                    this.quantity = quantity;
                    this.item = item;
                }

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public String getItem() {
                    return item;
                }

                public void setItem(String item) {
                    this.item = item;
                }
            }
        }
    }
}
