package com.example.orderservice.controller;

import com.example.orderservice.model.OrderDtoItems;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderLineRepository;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory",fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderDtoItems orderDtoItems){
        return CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderDtoItems));
    }

    public CompletableFuture<String> fallbackMethod(OrderDtoItems orderDtoItems,RuntimeException exception){
        return CompletableFuture.supplyAsync(()->"Something went wrong, Please Try again later");
    }
}
