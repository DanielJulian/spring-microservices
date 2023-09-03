package com.dannyjulian.matchservice.model;

import com.dannyjulian.matchservice.util.BidOrAsk;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String guid;
    private BigDecimal price;
    private Integer quantity;
    private BidOrAsk bidOrAsk;
}
