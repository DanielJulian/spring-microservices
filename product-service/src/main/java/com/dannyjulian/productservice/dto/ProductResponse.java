package com.dannyjulian.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductResponse {
    // Its a good practise to separate the model entities from the controller entities, thats why we are creating a similar class as in model.Product
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
