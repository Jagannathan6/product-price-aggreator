package com.exercise.product.aggregator.service;

import com.exercise.product.aggregator.domain.PricingResponse;
import com.exercise.product.aggregator.domain.ProductPricingModel;
import com.exercise.product.aggregator.domain.ProductResponse;
import com.exercise.product.aggregator.exception.GenericException;
import com.exercise.product.aggregator.exception.NotFoundException;
import com.exercise.product.aggregator.exception.domain.NotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductPricingFacade {

    @Autowired
    ProductService productService;

    public Mono<ProductPricingModel> getProductPricing(String productId) {

        Mono<ClientResponse> productClientResponseTuple =
                productService.getProductInformation(productId);

        Mono<ClientResponse> pricingClientResponseTuple =
                productService.getPricingInformation(productId);


        return Mono.zip(productClientResponseTuple,
                pricingClientResponseTuple).flatMap(tuple -> {
            ClientResponse productClientResponse = tuple.getT1();
            ClientResponse pricingClientResponse = tuple.getT2();

            log.info("Inside information {} ", productClientResponse.statusCode().value());

            if (productClientResponse.statusCode().value() == 404) {
                return productClientResponse.bodyToMono(NotFound.class)
                        .flatMap(msg -> {
                            log.error(" 404 error received while calling API {} and  Message {}" ,"Product Service API",
                                    msg.getErrorMsg() );
                            return Mono.error(new NotFoundException(msg.getErrorMsg()));
                        });
            }


            if (pricingClientResponse.statusCode().value() == 404) {
                return pricingClientResponse.bodyToMono(NotFound.class)
                        .flatMap(msg -> {
                            log.error(" 404 error received while calling API {} and  Message {}" ,"Pricing Service API",
                                    msg.getErrorMsg() );
                            return Mono.error(new NotFoundException(msg.getErrorMsg()));
                        });
            }

            if (productClientResponse.statusCode().is5xxServerError()) {
                return   productClientResponse.bodyToMono(String.class).flatMap( msg -> {
                    log.info(" Received 5XX error for the api {} and the error message is {}", "Product Service API",
                            msg);
                    return Mono.error(new GenericException(msg));
                });
            }

            Mono<ProductResponse> productResponseMono = productClientResponse.bodyToMono(ProductResponse.class);

            Mono<PricingResponse> pricingResponseMono = pricingClientResponse.bodyToMono(PricingResponse.class);

            return Mono.zip(productResponseMono, pricingResponseMono).flatMap( response -> {
                ProductResponse api1Response = response.getT1();
                PricingResponse api2Response = response.getT2();
                ProductPricingModel productPricingModel = ProductPricingModel.builder().build();
                log.info("api1Response 1 {}", api1Response);
                log.info("api2Response 2 {}", api2Response);
                BeanUtils.copyProperties(api1Response, productPricingModel);
                productPricingModel.setPrice(api2Response);
                return Mono.just(productPricingModel);
            });

        });

    }

}
