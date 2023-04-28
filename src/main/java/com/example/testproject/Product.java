package com.example.testproject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Product(Integer id, String name, BigDecimal price, Boolean availability) {
}

