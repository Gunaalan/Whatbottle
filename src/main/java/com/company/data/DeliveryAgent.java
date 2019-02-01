package com.company.data;

import java.util.Date;


public class DeliveryAgent {

    private String id;
    private Location currentLocation;
    private Date lastOrderedTime;
    private boolean isAssigned;

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Date getLastOrderedTime() {
        return lastOrderedTime;
    }

    public void setLastOrderedTime(Date lastOrderedTime) {
        this.lastOrderedTime = lastOrderedTime;
    }

    public DeliveryAgent(String id, Location currentLocation, Date lastOrderedTime, boolean isAssigned) {
        super();
        this.id = id;
        this.currentLocation = currentLocation;
        this.lastOrderedTime = lastOrderedTime;
        this.isAssigned = isAssigned;
    }
    
    
    
}
