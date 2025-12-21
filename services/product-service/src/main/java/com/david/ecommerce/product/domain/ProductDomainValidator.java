package com.david.ecommerce.product.domain;

import com.david.ecommerce.product.exception.DomainValidationException;

import java.math.BigDecimal;

public class ProductDomainValidator {

    public static void validate(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new DomainValidationException("Product name is required");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainValidationException("Product price must be greater than zero");
        }

        if (product.getSku() == null || product.getSku().isBlank()) {
            throw new DomainValidationException("SKU is required");
        }

        if (product.getCategory() == null) {
            throw new DomainValidationException("Category is required");
        }
    }
}
