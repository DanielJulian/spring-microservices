package com.dannyjulian.injector;

import com.dannyjulian.injector.dto.OrderItemDTO;
import com.dannyjulian.injector.dto.OrderRequest;
import com.dannyjulian.injector.util.BidOrAsk;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class Injector implements Runnable {

    private final WebClient webClient;

    private final Random rnd = new Random();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();;

    public Injector(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    public void startExecutorService() {
        log.info("Starting Injector Service");
        executor.submit(this);
    }

    @PreDestroy
    public void stopExecutorService() {
        log.info("Stopping Injector Service");
        executor.shutdownNow();
    }

    @Override
    public void run() {
        try {
            while (true) {
                OrderRequest request = createRandomOrderRequest();

                log.info("Sending request {}", request);
                String response = webClient.post()
                        .uri("http://localhost:8081/api/order")
                        .body(Mono.just(request), OrderRequest.class)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                log.info("Response: " + response);

                Thread.sleep(getRandomBetween(1, 1000));
            }
        } catch (Exception e) {
            log.error("An error occurred", e);
        }
    }

    private OrderRequest createRandomOrderRequest() {
        OrderRequest request = new OrderRequest();
        request.setUserEmail(getRandomEmail());

        List<OrderItemDTO> orderItems = new LinkedList<>();

        IntStream.range(0, getRandomBetween(1,5))
                .forEach(i -> orderItems.add(createRandomOrderItem()));

        request.setOrderItems(orderItems);

        return request;
    }

    private OrderItemDTO createRandomOrderItem() {
        OrderItemDTO orderItem = new OrderItemDTO();
        orderItem.setPrice(BigDecimal.valueOf(getRandomBetween(1, 500)));
        orderItem.setQuantity(getRandomBetween(1, 70));
        orderItem.setGuid(getRandomGuid());
        orderItem.setBidOrAsk(getRandomBidOrAsk());
        return orderItem;
    }

    private BidOrAsk getRandomBidOrAsk() {
        BidOrAsk[] bidOrAsks = BidOrAsk.values();
        return bidOrAsks[getRandomBetween(0, bidOrAsks.length)];
    }


    private String getRandomEmail() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt + "@gmail.com";
    }

    private String getRandomGuid() {
        String SALTCHARS = "ABCD";
        StringBuilder salt = new StringBuilder();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }



    private int getRandomBetween(int low, int high) {
        return rnd.nextInt(high-low) + low;
    }
}
