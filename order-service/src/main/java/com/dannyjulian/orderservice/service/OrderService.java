package com.dannyjulian.orderservice.service;

import com.dannyjulian.orderservice.dto.MatchRequest;
import com.dannyjulian.orderservice.dto.MatchResponse;
import com.dannyjulian.orderservice.dto.OrderRequest;
import com.dannyjulian.orderservice.dto.OrderItemDTO;
import com.dannyjulian.orderservice.model.Order;
import com.dannyjulian.orderservice.model.OrderItem;
import com.dannyjulian.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ApplicationEventPublisher applicationEventPublisher;


    public String placeOrder(OrderRequest orderRequest) {
        List<OrderItem> orderItems = orderRequest.getOrderItems()
                .stream()
                .map(this::mapToDto)
                .toList();

        Order order = new Order();
        order.setUserEmail(orderRequest.getUserEmail());
        order.setOrderItems(orderItems);

        orderRepository.save(order);

        List<MatchResponse> responses = new LinkedList<>();

        // TODO improve this so we send just 1 request with all the orderItems to the MatchService
        for (OrderItem item: orderItems) {

            MatchRequest request = getMatchRequest(item);

            // Call Match service and send notification message only if match was done.
            MatchResponse matchResponse = webClientBuilder.build().post()
                    .uri("http://match-service/api/match")
                    .body(Mono.just(request), MatchRequest.class)
                    .retrieve()
                    .bodyToMono(MatchResponse.class)
                    .block();

            responses.add(matchResponse);
        }

        if (responses.stream().allMatch(MatchResponse::isMatchSucceeded)) {
            // publish Order Placed Event
            applicationEventPublisher.publishEvent(orderRequest);
            return "All orders were placed and matched";
        } else {
            return "All orders were placed but not all matched";
        }

    }

    private MatchRequest getMatchRequest(OrderItem orderItem) {
        var matchRequest = new MatchRequest();
        matchRequest.setGuid(orderItem.getGuid());
        matchRequest.setPrice(orderItem.getPrice());
        matchRequest.setQuantity(orderItem.getQuantity());
        matchRequest.setBidOrAsk(orderItem.getBidOrAsk());
        return matchRequest;
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
