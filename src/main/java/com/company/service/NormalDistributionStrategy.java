package com.company.service;

import com.company.data.DeliveryAgent;
import com.company.data.DistributionType;
import com.company.data.Order;
import com.company.exception.SwiggyException;
import com.company.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
public class NormalDistributionStrategy implements DistributionStrategy {

    @Autowired
    DistributionService distributionService;


    @Override
    @PostConstruct
    public void register() {
        distributionService.register(DistributionType.NORMAL, this);
    }

    @Override
    public void distribute(Order order, List<DeliveryAgent> agentList) {
        double maxValue = Long.MIN_VALUE;
        DeliveryAgent assigned = null;
        for (DeliveryAgent deliveryAgent : agentList) {
            if (!deliveryAgent.isAssigned()) {
                double daWaitingTime = distributionService.findDAWaitingTime(deliveryAgent.getLastOrderedTime());
                double firstMile = distributionService.findFirstMile(order.getRestaurantLocation(), deliveryAgent.getCurrentLocation());
                double orderDelay = distributionService.findOrderDelay(order.getOrderTime());
                double cummulative = (1)*Constants.ORDER_DELAY_TIME_PROP_CONSTANT * (orderDelay) +
                        (1)*Constants.FIRST_MILE_PROP_CONSTANT / firstMile +
                        (1)*Constants.DA_WAITING_TIME_PROP_CONSTANT * daWaitingTime;      //we have kept three separate constants to multiply, to normalize the parameter to a fixed standard

                if (cummulative > maxValue) {
                    assigned = deliveryAgent;
                }
            }
        }
        if (assigned != null) {
            order.setDeliveryAgent(assigned);
            assigned.setAssigned(true);
            assigned.setLastOrderedTime(new Date());    //ideally to be updated after delivery
        } else {
            throw new SwiggyException("DeliveryAgent couldn't be assigned");
        }

    }


}
