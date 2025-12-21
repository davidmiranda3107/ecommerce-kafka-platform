package com.david.ecommerce.product.domain;

import com.david.ecommerce.product.exception.DomainValidationException;

public class CategoryDomainValidator {

    public static void validate(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new DomainValidationException("Category name is required");
        }
    }
}
