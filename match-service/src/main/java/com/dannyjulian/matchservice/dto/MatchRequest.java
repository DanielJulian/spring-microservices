package com.dannyjulian.matchservice.dto;

import com.dannyjulian.matchservice.util.BidOrAsk;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchRequest {

    private String guid;
    private BigDecimal price;
    private Integer quantity;
    private BidOrAsk bidOrAsk;
}
