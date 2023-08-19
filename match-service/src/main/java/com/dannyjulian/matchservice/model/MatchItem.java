package com.dannyjulian.matchservice.model;

import com.dannyjulian.matchservice.util.BidOrAsk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String guid;
    private BigDecimal price;
    private Integer quantity;
    private BidOrAsk bidOrAsk;
}
