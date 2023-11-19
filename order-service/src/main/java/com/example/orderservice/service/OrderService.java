package com.example.orderservice.service;
import com.example.orderservice.event.OrderPlaced;
import com.example.orderservice.model.*;
import com.example.orderservice.repository.OrderRepository;
import org.apache.kafka.common.message.OffsetCommitRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private KafkaTemplate<String,OrderPlaced> kafkaTemplate;
    public String placeOrder(OrderDtoItems orderDtoItems){
        OrderList order = new OrderList();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList= orderDtoItems
                .getOrderItemsDtoList()
                .stream()
                .map(this::mapToDTO)
                .toList();
        order.setOrderLineItemsList(orderLineItemsList);

        // collecting all the skuCodes from the orderLineItems
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // calling the inventory response to check whether the product is in stock or not using web client for synchronous communication
        InventoryResponse[] inventoryResponses = webClient.build().get().uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve().bodyToMono(InventoryResponse[].class).block();

        // after getting the inventory response from the inventory service, order service will proceed to save the orders in repo
        assert inventoryResponses != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (allProductsInStock) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",new OrderPlaced(order.getOrderNumber()));
            return "Order placed successfully!";
        }

        else {
            throw new IllegalArgumentException("Product is not available at this moment. Please try later!");
        }
    }
    private OrderLineItems mapToDTO(OrderDTO orderDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderDTO.getQuantity());
        orderLineItems.setPrice(orderDTO.getPrice());
        orderLineItems.setSkuCode(orderDTO.getSkuCode());
        return orderLineItems;
    }
}
