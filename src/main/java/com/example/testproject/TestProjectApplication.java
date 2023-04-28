package com.example.testproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;



@SpringBootApplication
@RestController
public class TestProjectApplication {

    private static final Logger log = LoggerFactory.getLogger(TestProjectApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TestProjectApplication.class, args);
    }


    @GetMapping("/product/{productId}/similarids")
    public ResponseEntity<Product[]> getProductDetail(@PathVariable String productId) {
        log.info("Getting product detail for productId= " + productId);
        Product[] products = this.getProductFromMockServer(productId);
        if (products.length == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(products);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public Product[] getProductFromMockServer(String productId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String[] similarIds = restTemplate.getForObject("http://localhost:3001/product/{productId}/similarids", String[].class, productId);
            return Arrays.stream(similarIds).map(id -> restTemplate.getForObject("http://localhost:3001/product/{productId}", Product.class, id)).toArray(Product[]::new);
        } catch (Exception e) {
            log.error("Error while getting product detail from mock server", e);
            return new Product[0];
        }
    }
}
