package com.dannyjulian.matchservice.dto;

import com.dannyjulian.matchservice.util.BidOrAsk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {

    private String guid;
    private BigDecimal price;
    private Integer quantity;
    private BidOrAsk bidOrAsk;
}
