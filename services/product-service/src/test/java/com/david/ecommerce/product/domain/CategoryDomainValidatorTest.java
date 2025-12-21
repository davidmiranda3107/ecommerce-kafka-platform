package com.david.ecommerce.product.domain;

import com.david.ecommerce.product.exception.DomainValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryDomainValidatorTest {

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        Category category = new Category();
        category.setId(1L);

        Assertions.assertThrows(DomainValidationException.class,
                () -> CategoryDomainValidator.validate(category));
    }
}
