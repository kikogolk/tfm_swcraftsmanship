package com.craftsmanship.tfm.dal.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.craftsmanship.tfm.dal.grpc.server.EntityConversion;
import com.craftsmanship.tfm.dal.grpc.server.GrpcServer;
import com.craftsmanship.tfm.dal.grpc.services.ItemPersistenceService;
import com.craftsmanship.tfm.dal.grpc.services.OrderPersistenceService;
import com.craftsmanship.tfm.dal.model.ItemDAO;
import com.craftsmanship.tfm.dal.model.OrderDAO;

import io.grpc.BindableService;
import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class DalConfig {

    @Autowired
    ItemDAO itemDAO;
    
    @Autowired
    OrderDAO orderDAO;

    @Autowired
    MeterRegistry meterRegistry;

    @Bean
    public GrpcServer grpcServer() {

        EntityConversion entityConversion = new EntityConversion();

        ItemPersistenceService itemService = new ItemPersistenceService(itemDAO, entityConversion, meterRegistry);
        OrderPersistenceService orderService = new OrderPersistenceService(orderDAO, entityConversion, meterRegistry);

        List<BindableService> services = new ArrayList<BindableService>();
        services.add(itemService);
        services.add(orderService);

        GrpcServer grpcServer = new GrpcServer(services);
        return grpcServer;
    }

}
