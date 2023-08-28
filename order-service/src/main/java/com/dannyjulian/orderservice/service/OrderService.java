package com.dannyjulian.orderservice.service;

import com.dannyjulian.orderservice.dto.OrderRequest;
import com.dannyjulian.orderservice.dto.OrderItemDTO;
import com.dannyjulian.orderservice.model.Order;
import com.dannyjulian.orderservice.model.OrderItem;
import com.dannyjulian.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;


    public void placeOrder(OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getOrderItems()
                .stream()
                .map(this::mapToDto)
                .toList();

        Order order = new Order();
        order.setUserEmail(orderRequest.getUserEmail());
        order.setOrderItems(orderItems);

        orderRepository.save(order);


    }



    private OrderItem mapToDto(OrderItemDTO orders) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(orders.getPrice());
        orderItem.setQuantity(orders.getQuantity());
        orderItem.setGuid(orders.getGuid());
        orderItem.setBidOrAsk(orders.getBidOrAsk());
        return orderItem;
    }
}
