package com.exercise.product.aggregator.resource;

import com.exercise.product.aggregator.domain.ProductPricingModel;
import com.exercise.product.aggregator.service.ProductPricingFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/v1/product_price")
@Slf4j
public class ProductPricingResource {

    @Autowired
    ProductPricingFacade productPricingFacade;

    @GetMapping(value = "/{product_id}", produces = "application/json")
    public Mono<ProductPricingModel> getProduct(@PathVariable("product_id") String productId) {
        log.info("Get Product and Price Information {}", productId);
        return productPricingFacade.getProductPricing(productId);
    }

}
