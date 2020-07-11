package com.exercise.product.aggregator.resource;

import com.exercise.product.aggregator.domain.ProductPricingModel;
import com.exercise.product.aggregator.service.ProductPricingFacade;
import com.exercise.product.aggregator.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductPricingResource.class)
public class ProductPricingResourceTest {

    @MockBean
    ProductPricingFacade productPricingFacade;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testGetProductPricing() {
        when(productPricingFacade.getProductPricing("abcdefgh")).thenReturn(Mono.just(TestDataUtils.getProductPricingModel()));
        FluxExchangeResult<ProductPricingModel> productResponse = webClient.get()
                .uri("/v1/product_price/abcdefgh")
                .exchange()
                .expectStatus().is2xxSuccessful().returnResult(ProductPricingModel.class);
        ProductPricingModel response = productResponse.getResponseBody().blockLast();
        Mockito.verify(productPricingFacade, times(1)).getProductPricing(any());
        assertTrue(StringUtils.isNotBlank(response.getId()));
    }

}