package com.exercise.product.aggregator.utils;


import com.exercise.product.aggregator.domain.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TestDataUtils {

    static String productId = "abcdefgh";

    public static ProductPricingModel getProductPricingModel() {
        ProductPricingModel productPricingModel = ProductPricingModel
                .builder().price(getPriceResponse()).build();
        BeanUtils.copyProperties(getProductResponse(), productPricingModel);
        return productPricingModel;
    }

    public static PricingResponse getPriceResponse() {
        return PricingResponse.builder()
                .productId(productId)
                .range("1-2")
                .maxPrice(5.20f)
                .minPrice(4.00f)
                .build();
    }



    public static ProductResponse getProductResponse() {
        Workflow workflow = Workflow.builder().status("NEW").build();
        MetaFields fields = MetaFields.builder().key("test").build();
        ProductResponse productResponse = ProductResponse
                .builder()
                .id(productId)
                .backOrder(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .publishedAt(LocalDateTime.now())
                .sellerId("124")
                .title("Choclate")
                .manufacturer("Nestle")
                .soldOut(true)
                .visible(false)
                .requiresShopping(false)
                .workflow(workflow)
                .metaFields(Arrays.asList(fields))
                .build();

        return productResponse;
    }



}

