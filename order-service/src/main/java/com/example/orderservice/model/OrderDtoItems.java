package com.example.orderservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDtoItems {
    private List<OrderDTO> orderItemsDtoList;
}
