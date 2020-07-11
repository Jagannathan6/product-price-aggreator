package com.exercise.product.aggregator.service;

import com.exercise.product.aggregator.utils.TestDataUtils;
import com.exercise.product.aggregator.domain.PricingResponse;
import com.exercise.product.aggregator.domain.ProductPricingModel;
import com.exercise.product.aggregator.domain.ProductResponse;
import com.exercise.product.aggregator.exception.GenericException;
import com.exercise.product.aggregator.exception.NotFoundException;
import com.exercise.product.aggregator.exception.domain.NotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProductPricingFacadeTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductPricingFacade productPricingFacade;

    String productId = "abcdefg";

    @Test
    public void testFetchPricing() {
        ClientResponse productResponse = mock(ClientResponse.class);
        ClientResponse pricingResponse = mock(ClientResponse.class);
        when(productResponse.statusCode()).thenReturn(HttpStatus.OK);
        when(pricingResponse.statusCode()).thenReturn(HttpStatus.OK);
        Mono<ClientResponse> productResponseMono = Mono.just(productResponse);
        Mono<ClientResponse> pricingResponseMono = Mono.just(pricingResponse);
        when( productService.getProductInformation(productId)).thenReturn(productResponseMono);
        when( productService.getPricingInformation(productId)).thenReturn(pricingResponseMono);
        when(productResponse.bodyToMono(ProductResponse.class)).thenReturn(Mono.just(TestDataUtils.getProductResponse()));
        when(pricingResponse.bodyToMono(PricingResponse.class)).thenReturn(Mono.just(TestDataUtils.getPriceResponse()));
        Mono<ProductPricingModel> productPricing = productPricingFacade.getProductPricing(productId);
        ProductPricingModel model = productPricing.block();
        assertNotNull(model);

    }

    @Test
    public void testFetchPricingWith404Product() {
        ClientResponse productResponse = mock(ClientResponse.class);
        ClientResponse pricingResponse = mock(ClientResponse.class);
        when(productResponse.statusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(pricingResponse.statusCode()).thenReturn(HttpStatus.OK);
        Mono<ClientResponse> productResponseMono = Mono.just(productResponse);
        Mono<ClientResponse> pricingResponseMono = Mono.just(pricingResponse);
        when(productResponse.bodyToMono(NotFound.class)).thenReturn(Mono.just(NotFound.builder().errorMsg("not found").build()));
        when( productService.getProductInformation(productId)).thenReturn(productResponseMono);
        when( productService.getPricingInformation(productId)).thenReturn(pricingResponseMono);
        when(productResponse.bodyToMono(ProductResponse.class)).thenReturn(Mono.just(TestDataUtils.getProductResponse()));
        when(pricingResponse.bodyToMono(PricingResponse.class)).thenReturn(Mono.just(TestDataUtils.getPriceResponse()));
        StepVerifier
                .create(productPricingFacade.getProductPricing(productId))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException
                        && throwable.getMessage().contains("not found"))
                .verify();
    }

    @Test
    public void testFetchPricingWith500Product() {
        ClientResponse productResponse = mock(ClientResponse.class);
        ClientResponse pricingResponse = mock(ClientResponse.class);
        when(productResponse.statusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        when(pricingResponse.statusCode()).thenReturn(HttpStatus.OK);
        Mono<ClientResponse> productResponseMono = Mono.just(productResponse);
        Mono<ClientResponse> pricingResponseMono = Mono.just(pricingResponse);
        when(productResponse.bodyToMono(String.class)).thenReturn(Mono.just("Internal Server error"));
        when( productService.getProductInformation(productId)).thenReturn(productResponseMono);
        when( productService.getPricingInformation(productId)).thenReturn(pricingResponseMono);
        when(productResponse.bodyToMono(ProductResponse.class)).thenReturn(Mono.just(TestDataUtils.getProductResponse()));
        when(pricingResponse.bodyToMono(PricingResponse.class)).thenReturn(Mono.just(TestDataUtils.getPriceResponse()));
        StepVerifier
                .create(productPricingFacade.getProductPricing(productId))
                .expectErrorMatches(throwable -> throwable instanceof GenericException)
                .verify();
    }


    @Test
    public void testFetchProductPricingWith400Error() {
        ClientResponse productResponse = mock(ClientResponse.class);
        ClientResponse pricingResponse = mock(ClientResponse.class);
        when(productResponse.statusCode()).thenReturn(HttpStatus.OK);
        when(pricingResponse.statusCode()).thenReturn(HttpStatus.NOT_FOUND);
        Mono<ClientResponse> productResponseMono = Mono.just(productResponse);
        Mono<ClientResponse> pricingResponseMono = Mono.just(pricingResponse);
        when(pricingResponse.bodyToMono(NotFound.class)).thenReturn(Mono.just(NotFound.builder().errorMsg("not found").build()));
        when( productService.getProductInformation(productId)).thenReturn(productResponseMono);
        when( productService.getPricingInformation(productId)).thenReturn(pricingResponseMono);
        when(productResponse.bodyToMono(ProductResponse.class)).thenReturn(Mono.just(TestDataUtils.getProductResponse()));
        when(pricingResponse.bodyToMono(PricingResponse.class)).thenReturn(Mono.just(TestDataUtils.getPriceResponse()));
        StepVerifier
                .create(productPricingFacade.getProductPricing(productId))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException
                        && throwable.getMessage().contains("not found"))
                .verify();

    }


}
