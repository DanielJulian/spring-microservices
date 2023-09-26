package com.dannyjulian.orderservice.listener;

import com.dannyjulian.orderservice.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedEventListener {

    private final KafkaTemplate<String, OrderRequest> kafkaTemplate;

    @EventListener
    public void handleOrderPlacedEvent(OrderRequest event) {
        log.info("Order Placed Event Received, Sending OrderRequest to notificationTopic: {}", event);
        kafkaTemplate.send("notificationTopic", event);
    }
}
