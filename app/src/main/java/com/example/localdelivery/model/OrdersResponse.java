package com.example.localdelivery.model;

import java.util.List;

public class OrdersResponse {
    private Result result;

    public OrdersResponse(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private List<Orders> orders;

        public Result(List<Orders> orders) {
            this.orders = orders;
        }

        public List<Orders> getOrders() {
            return orders;
        }

        public void setOrders(List<Orders> orders) {
            this.orders = orders;
        }

        public static class Orders {
            private List<Order> order;
            private String _id;

            public Orders(List<Order> order, String _id) {
                this.order = order;
                this._id = _id;
            }

            public List<Order> getOrder() {
                return order;
            }

            public void setOrder(List<Order> order) {
                this.order = order;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public static class Order {
                private String status;
                private boolean pickUp;
                private String paymentType;
                private String otp;
                private String _id;
                private List<Items> items;
                private ShopId shopId;
                private String totalPrice;
                private String createdAt;
                private String updatedAt;

                public Order(String status, boolean pickUp, String paymentType, String otp, String _id, List<Items> items, ShopId shopId,
                             String totalPrice, String createdAt, String updatedAt) {
                    this.status = status;
                    this.pickUp = pickUp;
                    this.paymentType = paymentType;
                    this.otp = otp;
                    this._id = _id;
                    this.items = items;
                    this.shopId = shopId;
                    this.totalPrice = totalPrice;
                    this.createdAt = createdAt;
                    this.updatedAt = updatedAt;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
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

                public String getOtp() {
                    return otp;
                }

                public void setOtp(String otp) {
                    this.otp = otp;
                }

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public List<Items> getItems() {
                    return items;
                }

                public void setItems(List<Items> items) {
                    this.items = items;
                }

                public ShopId getShopId() {
                    return shopId;
                }

                public void setShopId(ShopId shopId) {
                    this.shopId = shopId;
                }

                public String getTotalPrice() {
                    return totalPrice;
                }

                public void setTotalPrice(String totalPrice) {
                    this.totalPrice = totalPrice;
                }

                public String getCreatedAt() {
                    return createdAt;
                }

                public void setCreatedAt(String createdAt) {
                    this.createdAt = createdAt;
                }

                public String getUpdatedAt() {
                    return updatedAt;
                }

                public void setUpdatedAt(String updatedAt) {
                    this.updatedAt = updatedAt;
                }

                public static class Items {
                    private Item item;
                    private String quantity;

                    public Items(Item item, String quantity) {
                        this.item = item;
                        this.quantity = quantity;
                    }

                    public Item getItem() {
                        return item;
                    }

                    public void setItem(Item item) {
                        this.item = item;
                    }

                    public String getQuantity() {
                        return quantity;
                    }

                    public void setQuantity(String quantity) {
                        this.quantity = quantity;
                    }

                    public static class Item {
                        private String _id;
                        private String name;
                        private String price;
                        private String type;
                        private String image;
                        private boolean available;
                        private String shop;
                        private String createdAt;
                        private String updatedAt;
                        private int _v;

                        public Item(String _id, String name, String price, String type, String image,
                                    boolean available, String shop, String createdAt, String updatedAt, int _v) {
                            this._id = _id;
                            this.name = name;
                            this.price = price;
                            this.type = type;
                            this.image = image;
                            this.available = available;
                            this.shop = shop;
                            this.createdAt = createdAt;
                            this.updatedAt = updatedAt;
                            this._v = _v;
                        }

                        public String get_id() {
                            return _id;
                        }

                        public void set_id(String _id) {
                            this._id = _id;
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

                        public String getImage() {
                            return image;
                        }

                        public void setImage(String image) {
                            this.image = image;
                        }

                        public boolean isAvailable() {
                            return available;
                        }

                        public void setAvailable(boolean available) {
                            this.available = available;
                        }

                        public String getShop() {
                            return shop;
                        }

                        public void setShop(String shop) {
                            this.shop = shop;
                        }

                        public String getCreatedAt() {
                            return createdAt;
                        }

                        public void setCreatedAt(String createdAt) {
                            this.createdAt = createdAt;
                        }

                        public String getUpdatedAt() {
                            return updatedAt;
                        }

                        public void setUpdatedAt(String updatedAt) {
                            this.updatedAt = updatedAt;
                        }

                        public int get_v() {
                            return _v;
                        }

                        public void set_v(int _v) {
                            this._v = _v;
                        }
                    }
                }

                public static class ShopId {
                    private ShopDetails shopDetails;

                    public ShopId(ShopDetails shopDetails) {
                        this.shopDetails = shopDetails;
                    }

                    public ShopDetails getShopDetails() {
                        return shopDetails;
                    }

                    public void setShopDetails(ShopDetails shopDetails) {
                        this.shopDetails = shopDetails;
                    }

                    public static class ShopDetails {
                        private String address;
                        private String latitude;
                        private String longitude;
                        private String shopName;
                        private String shopType;

                        public ShopDetails(String address, String latitude, String longitude, String shopName,
                                           String shopType) {
                            this.address = address;
                            this.latitude = latitude;
                            this.longitude = longitude;
                            this.shopName = shopName;
                            this.shopType = shopType;
                        }

                        public String getAddress() {
                            return address;
                        }

                        public void setAddress(String address) {
                            this.address = address;
                        }

                        public String getLatitude() {
                            return latitude;
                        }

                        public void setLatitude(String latitude) {
                            this.latitude = latitude;
                        }

                        public String getLongitude() {
                            return longitude;
                        }

                        public void setLongitude(String longitude) {
                            this.longitude = longitude;
                        }

                        public String getShopName() {
                            return shopName;
                        }

                        public void setShopName(String shopName) {
                            this.shopName = shopName;
                        }

                        public String getShopType() {
                            return shopType;
                        }

                        public void setShopType(String shopType) {
                            this.shopType = shopType;
                        }
                    }
                }
            }
        }
    }
}
