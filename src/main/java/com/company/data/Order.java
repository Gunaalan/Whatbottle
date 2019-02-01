package com.company.data;

import java.util.Date;

public class Order {

    private String id;
    private Location location;
    private DeliveryAgent deliveryAgent;
    private Date orderTime;

    private Location restaurantLocation;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DeliveryAgent getDeliveryAgent() {
        return deliveryAgent;
    }

    public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Order(String id, Location location, Date orderTime, DeliveryAgent deliveryAgent,Location restaurantLocation) {
        super();
        this.id = id;
        this.location = location;
        this.orderTime = orderTime;
        this.deliveryAgent = deliveryAgent;
        this.restaurantLocation = restaurantLocation;
    }


    public Order() {

    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", location=" + location + ", orderTime=" + orderTime + "]";
    }

    public Location getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(Location restaurantLocation) {
        restaurantLocation = restaurantLocation;
    }
}
