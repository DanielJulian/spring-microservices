package com.dannyjulian.injector.dto;

import com.dannyjulian.injector.util.BidOrAsk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private String guid;
    private BigDecimal price;
    private Integer quantity;
    private BidOrAsk bidOrAsk;
}
