package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDTO;
import com.example.demo.model.ProductResponse;
import com.example.demo.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public void createProduct(ProductDTO productDTO){
        Product product = Product.builder().name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription()).build();
        productRepository.save(product);
        log.info("Product {} is saved",product.getId());

    }

    public List<ProductResponse> retrieveProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }
    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder().name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .id(product.getId())
                .build();
    }

}
