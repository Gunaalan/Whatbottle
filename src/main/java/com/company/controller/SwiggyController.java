package com.company.controller;

import com.company.data.DeliveryAgent;
import com.company.data.DistributionType;
import com.company.data.Location;
import com.company.data.Order;
import com.company.service.DistributionService;
import com.company.service.DistributionStrategy;
import com.company.util.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * Created by gunaass
 */
@Controller
public class SwiggyController {

    @Autowired
    DistributionService distributionService;


    @RequestMapping(value = "api/v1/swiggy/run", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> startDistribution() {

        List<Order> orderList = new ArrayList<>();


        // String id, Location location, Date orderTime, DeliveryAgent deliveryAgent, restaurantLocation

        orderList.add(new Order("1", new Location(20.9697, 70.80322), new Date(System.currentTimeMillis() - (Constants.oneHour)), null, new Location(21.287, 71.80322)));
        orderList.add(new Order("2", new Location(30.9697, 71.80322), new Date(System.currentTimeMillis() - (Constants.twoHour)), null, new Location(21.287, 71.80322)));
        orderList.add(new Order("3", new Location(32.9697, 71.80322), new Date(System.currentTimeMillis() - (Constants.twoAndHafHour)), null, new Location(21.287, 71.80322)));


        List<DeliveryAgent> agentList = new ArrayList<>();

        //String id, Location currentLocation, Date lastOrderedTime, boolean isAssigned

        agentList.add(new DeliveryAgent("d1", new Location(20.9658, 70.80323), new Date(System.currentTimeMillis() - (Constants.oneHour)), false));
        agentList.add(new DeliveryAgent("d2", new Location(21.9658, 70.90323), new Date(System.currentTimeMillis() - (Constants.oneHour)), false));
        agentList.add(new DeliveryAgent("d3", new Location(22.9658, 71.80323), new Date(System.currentTimeMillis() - (Constants.oneHour)), false));
        agentList.add(new DeliveryAgent("d4", new Location(23.9658, 72.80323), new Date(System.currentTimeMillis() - (Constants.oneHour)), false));

        for (Order order : orderList) {
            DistributionStrategy distributionStrategy = distributionService.getStrategy(DistributionType.DAASSIGNMENT_DELAY_PRIORTIZED);
            distributionStrategy.distribute(order, agentList);
        }

        for (Order order : orderList) {
            System.out.println("----------------------------");
            System.out.println("order id " + order.getId());
            if (order.getDeliveryAgent() != null) {
                System.out.println("DeilveryAgent assigned " + order.getDeliveryAgent().getId());
            }
        }
        return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
    }

}
