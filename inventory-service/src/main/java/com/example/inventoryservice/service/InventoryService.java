package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Transactional (readOnly = true)
    public boolean isInStock(String skuCode){
        Optional<Inventory> inventory =inventoryRepository.findBySkuCode(skuCode);
        return inventory.isPresent();
    }
}
