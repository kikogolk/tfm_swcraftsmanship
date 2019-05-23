package com.craftsmanship.tfm.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.craftsmanship.tfm.exceptions.CustomException;
import com.craftsmanship.tfm.idls.v2.OrderPersistenceServiceGrpc;
import com.craftsmanship.tfm.idls.v2.ItemPersistence.Empty;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.CreateOrderRequest;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.CreateOrderResponse;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.DeleteOrderRequest;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.DeleteOrderResponse;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.GetOrderRequest;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.GetOrderResponse;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.GrpcOrder;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.ListOrderResponse;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.UpdateOrderRequest;
import com.craftsmanship.tfm.idls.v2.OrderPersistence.UpdateOrderResponse;
import com.craftsmanship.tfm.idls.v2.OrderPersistenceServiceGrpc.OrderPersistenceServiceBlockingStub;
import com.craftsmanship.tfm.models.Order;
import com.craftsmanship.tfm.utils.ConversionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class OrderPersistenceGrpcClient {
    private static final Logger logger = LoggerFactory.getLogger(OrderPersistenceGrpcClient.class);

    private final ManagedChannel channel;
    private final OrderPersistenceServiceBlockingStub blockingStub;

    /**
     * Construct client for accessing OrderPersistenceService server at
     * {@code host:port}.
     */
    public OrderPersistenceGrpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    /**
     * Construct client for accessing OrderPersistenceService server using the
     * existing channel.
     */
    public OrderPersistenceGrpcClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = OrderPersistenceServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws CustomException, InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public Order create(Order order) throws CustomException {
        logger.info("Creating Order");

        GrpcOrder grpcOrder = ConversionUtils.getGrpcOrderFromOrder(order);

        CreateOrderRequest request = CreateOrderRequest.newBuilder().setOrder(grpcOrder).build();

        Order orderReceived = null;
        try {
            CreateOrderResponse response = blockingStub.create(request);
            orderReceived = ConversionUtils.getOrderFromGrpcOrder(response.getOrder());
        } catch (StatusRuntimeException e) {
            logger.error("Exception creating Order: " + e.getMessage());
            Status status = Status.fromThrowable(e);
            if (status.getCode() == Status.Code.INTERNAL) {
                throw new CustomException(status.getDescription());
            } else {
                throw new CustomException("UNKNOWN ERROR");
            }
        }

        return orderReceived;
    }

    public List<Order> list() throws CustomException {
        logger.info("List all Orders");

        Empty request = Empty.newBuilder().build();

        List<Order> result = new ArrayList<>();
        try {
            ListOrderResponse response = blockingStub.list(request);
            List<GrpcOrder> ordersResponse = response.getListOfOrdersList();
    
            for (GrpcOrder grpcOrder : ordersResponse) {
                result.add(ConversionUtils.getOrderFromGrpcOrder(grpcOrder));
            }
        } catch (StatusRuntimeException e) {
            logger.error("Exception listing orders: " + e.getMessage());
            Status status = Status.fromThrowable(e);
            if (status.getCode() == Status.Code.INTERNAL) {
                throw new CustomException(status.getDescription());
            } else {
                throw new CustomException("UNKNOWN ERROR");
            }
        }

        return result;
    }

    public Order get(Long id) throws CustomException {
        logger.info("Get order with id: " + id);

        GetOrderRequest request = GetOrderRequest.newBuilder().setId(id).build();

        Order order = null;
        try {
            GetOrderResponse response = blockingStub.get(request);
            order = ConversionUtils.getOrderFromGrpcOrder(response.getOrder());
        } catch (StatusRuntimeException e) {
            logger.error("Exception getting order with id " + id + ": " + e.getMessage());
            Status status = Status.fromThrowable(e);
            if (status.getCode() == Status.Code.INTERNAL) {
                throw new CustomException(status.getDescription());
            } else {
                throw new CustomException("UNKNOWN ERROR");
            }
        }

        return order;
    }

    public Order update(Long id, Order order) throws CustomException {
        logger.info("Updating order with id: " + id);

        UpdateOrderRequest request = UpdateOrderRequest.newBuilder().setId(id)
                .setOrder(ConversionUtils.getGrpcOrderFromOrder(order)).build();

        Order updatedOrder = null;
        try {
            UpdateOrderResponse response = blockingStub.update(request);
            updatedOrder = ConversionUtils.getOrderFromGrpcOrder(response.getOrder());
        } catch (StatusRuntimeException e) {
            logger.error("Exception updating order with id " + id + ": " + e.getMessage());
            Status status = Status.fromThrowable(e);
            if (status.getCode() == Status.Code.INTERNAL) {
                throw new CustomException(status.getDescription());
            } else {
                throw new CustomException("UNKNOWN ERROR");
            }
        }

        return updatedOrder;
    }

    public Order delete(Long id) throws CustomException {
        logger.info("Deleting order with id: " + id);

        DeleteOrderRequest request = DeleteOrderRequest.newBuilder().setId(id).build();

        Order order = null;
        try {
            DeleteOrderResponse response = blockingStub.delete(request);
            order = ConversionUtils.getOrderFromGrpcOrder(response.getOrder());
        } catch (StatusRuntimeException e) {
            logger.error("Exception deleting order with id " + id + ": " + e.getMessage());
            Status status = Status.fromThrowable(e);
            if (status.getCode() == Status.Code.INTERNAL) {
                throw new CustomException(status.getDescription());
            } else {
                throw new CustomException("UNKNOWN ERROR");
            }
        }

        return order;
    }
}