package com.example.orderservice.controller;

import com.example.orderservice.model.OrderDtoItems;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderLineRepository;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderDtoItems orderDtoItems){
        orderService.placeOrder(orderDtoItems);
        return "Order placed successfully!";
    }
}
