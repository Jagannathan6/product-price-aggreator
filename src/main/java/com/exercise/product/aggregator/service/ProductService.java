package com.exercise.product.aggregator.service;

import com.exercise.product.aggregator.config.AppConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Setter
@Getter
public class ProductService {

    @Autowired
    AppConfig appConfig;

    private WebClient webClient;

    @Autowired
    public ProductService() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    public Mono<ClientResponse> getProductInformation(String productId) {
        String apiName = "/v1/products/" + productId;
        return webClient.get()
                .uri(appConfig.getHost() + apiName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    public Mono<ClientResponse> getPricingInformation(String productId) {
        String apiName = "/v1/products/" + productId+"/prices";
        return webClient.get()
                .uri(appConfig.getHost() + apiName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

}
