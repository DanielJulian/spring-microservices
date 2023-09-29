package com.dannyjulian.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /*
    * @LoadBalanced Annotation explained
    * This app (Order service) calls another app (match service) through HTTP.
    * Since we are using eureka for service discovery, we have to set the @LoadBalanced
    * annotation to tell the WebClient that, whenever there are many available match-services instances to call,
    * just pick one of them. If we dont put this annotation, the webclient will be confused and won't know which instance to call
    * */

}
