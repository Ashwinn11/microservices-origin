package com.example.orderservice.service;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderDTO;
import com.example.orderservice.model.OrderDtoItems;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public void placeOrder(OrderDtoItems orderDtoItems){
        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList= orderDtoItems
                .getOrderItemsDtoList()
                .stream()
                .map(this::mapToDTO)
                .toList();
        order.setOrderItems(orderLineItemsList);
        orderRepository.save(order);
    }
    private OrderLineItems mapToDTO(OrderDTO orderDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderDTO.getQuantity());
        orderLineItems.setPrice(orderDTO.getPrice());
        orderLineItems.setSkuCode(orderDTO.getSkuCode());
        return orderLineItems;
    }

}
