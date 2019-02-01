package com.company.service;


import com.company.data.DeliveryAgent;
import com.company.data.DistributionType;
import com.company.data.Location;
import com.company.data.Order;
import com.company.util.Constants;
import com.company.util.SwiggyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


@Service
public class DistributionServiceImpl implements DistributionService{

    @Autowired
    SwiggyUtils swiggyUtils;

    private Map<DistributionType, DistributionStrategy> distributionStrategymap = new EnumMap<>(DistributionType.class);

    public void register(DistributionType type, DistributionStrategy strategy) {
        distributionStrategymap.put(type, strategy);
    }

    public void assignDeliveryAgent(Order order, List<DeliveryAgent> agentList, DistributionType type) {
        distributionStrategymap.get(type).distribute(order, agentList);
        //Based on the strategy from the order , we do the distribution here.
    }

    public DistributionStrategy getStrategy( DistributionType type) {
       return distributionStrategymap.get(type);
    }


    public double findFirstMile(Location restaurantLocation, Location DAcurrentLocation ) {
        double distance = swiggyUtils.calculateDistance(restaurantLocation , DAcurrentLocation);
        double time =  (distance/ Constants.SPEED);
        return time;
    }

    public long findDAWaitingTime(Date lastOrderedTime) {
        long milisec =  System.currentTimeMillis() - lastOrderedTime.getTime();
        return (milisec/100/60);
    }

    public  long findOrderDelay(Date orderTime) {
        long milisec =  System.currentTimeMillis() - orderTime.getTime();
        return (milisec/100/60);
    }
}
