package com.exercise.product.aggregator.domain;

import com.exercise.product.aggregator.constants.ProductConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductPricingModel {

    @JsonProperty("product_id")
    private String id;

    private String sellerId;

    private String title;

    private String manufacturer;

    private boolean lowQuantity;

    private boolean soldOut;

    private boolean backOrder;

    private boolean visible;

    private boolean requiresShopping;

    private List<MetaFields> metaFields;

    private Workflow workflow;

    @JsonFormat(pattern = ProductConstants.DATE_FORMAT)
    private LocalDateTime publishedAt;

    @JsonFormat(pattern = ProductConstants.DATE_FORMAT)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = ProductConstants.DATE_FORMAT)
    private LocalDateTime updatedAt;

    private PricingResponse price;

}