package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    // order service will call the isInStock method to check the products are in stock or not and it will response with inventory response
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
