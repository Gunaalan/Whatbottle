package com.company.service;

import com.company.data.DeliveryAgent;
import com.company.data.Location;
import com.company.data.Order;
import com.company.util.Constants;
import com.company.util.SwiggyUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public interface DistributionStrategy {

    public void register();
    
    public void distribute(Order order , List<DeliveryAgent> agentList);


}
