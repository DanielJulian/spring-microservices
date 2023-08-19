package com.dannyjulian.matchservice.dto;

import com.dannyjulian.matchservice.util.BidOrAsk;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResponse {
    private String guid;
    private BigDecimal price;
    private Integer quantity;
    private BidOrAsk bidOrAsk;
    private boolean matchSucceeded;
}
