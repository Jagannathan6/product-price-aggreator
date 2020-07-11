package com.exercise.product.aggregator.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class AppConfig {

    @Value("${products.host}")
    private String host;

    
}
