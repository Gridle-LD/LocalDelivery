package com.gridle.localdelivery.model;

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
            private String totalPrice;
            private boolean pickUp;
            private String paymentType;
            private String time;

            public Shop(String shopId, List<Items> items, String totalPrice, boolean pickUp, String paymentType,
                        String time) {
                this.shopId = shopId;
                this.items = items;
                this.totalPrice = totalPrice;
                this.pickUp = pickUp;
                this.paymentType = paymentType;
                this.time = time;
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

            public String getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(String totalPrice) {
                this.totalPrice = totalPrice;
            }

            public boolean isPickUp() {
                return pickUp;
            }

            public void setPickUp(boolean pickUp) {
                this.pickUp = pickUp;
            }

            public String getPaymentType() {
                return paymentType;
            }

            public void setPaymentType(String paymentType) {
                this.paymentType = paymentType;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public static class Items {
                private String quantity;
                private String item;
                private String name;
                private String price;
                private String type;

                public Items(String quantity, String item, String name, String price, String type) {
                    this.quantity = quantity;
                    this.item = item;
                    this.name = name;
                    this.price = price;
                    this.type = type;
                }

//                public Items(String quantity, String item) {
//                    this.quantity = quantity;
//                    this.item = item;
//                }

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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
    }
}
