package com.example.orderservice.service;
import com.example.orderservice.model.OrderDTO;
import com.example.orderservice.model.OrderDtoItems;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.model.OrderList;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient webClient;
    public void placeOrder(OrderDtoItems orderDtoItems){
        OrderList order = new OrderList();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList= orderDtoItems
                .getOrderItemsDtoList()
                .stream()
                .map(this::mapToDTO)
                .toList();
        order.setOrderLineItemsList(orderLineItemsList);
        Boolean result = webClient.get().uri("http://localhost:8081/api/inventory")
                        .retrieve().bodyToMono(Boolean.class).block();
        if (Boolean.TRUE.equals(result)) orderRepository.save(order);
        else throw new IllegalArgumentException("Product is not available at this moment. Please try later!");
    }
    private OrderLineItems mapToDTO(OrderDTO orderDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderDTO.getQuantity());
        orderLineItems.setPrice(orderDTO.getPrice());
        orderLineItems.setSkuCode(orderDTO.getSkuCode());
        return orderLineItems;
    }

}
