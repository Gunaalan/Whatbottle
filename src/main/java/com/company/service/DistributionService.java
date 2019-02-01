package com.company.service;

import com.company.data.DeliveryAgent;
import com.company.data.DistributionType;
import com.company.data.Location;
import com.company.data.Order;

import java.util.Date;
import java.util.List;

/**
 * Created by guna on 30/06/18.
 */
public interface DistributionService {
    public void register(DistributionType type, DistributionStrategy strategy);

    public void assignDeliveryAgent(Order order, List<DeliveryAgent> agentList, DistributionType type);

    public DistributionStrategy getStrategy( DistributionType type);

    public double findFirstMile(Location restaurantLocation, Location DAcurrentLocation);

    public long findDAWaitingTime(Date lastOrderedTime);

    public  long findOrderDelay(Date orderTime);

}
