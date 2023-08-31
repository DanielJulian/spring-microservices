package com.dannyjulian.orderservice.controller;

import com.dannyjulian.orderservice.dto.OrderRequest;
import com.dannyjulian.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor // Creates a constructor at compile time for the class variables. This helps with dependency injection.
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Place Order request received: " + orderRequest);
        return orderService.placeOrder(orderRequest);
    }


}
