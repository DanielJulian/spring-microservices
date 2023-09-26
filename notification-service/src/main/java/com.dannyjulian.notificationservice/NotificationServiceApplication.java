package com.dannyjulian.notificationservice;

import com.dannyjulian.notificationservice.dto.OrderRequest;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceApplication {

    private final ObservationRegistry observationRegistry;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderRequest orderPlacedEvent) {
        log.info("Got message <{}>", orderPlacedEvent);
        // TODO send out an email notification
    }
}
