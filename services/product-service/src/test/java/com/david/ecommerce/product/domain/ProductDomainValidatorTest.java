package com.david.ecommerce.product.domain;

import com.david.ecommerce.product.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductDomainValidatorTest {

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(10));

        assertThrows(DomainValidationException.class,
                () -> ProductDomainValidator.validate(product));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsInvalid() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(BigDecimal.ZERO);

        assertThrows(DomainValidationException.class,
                () -> ProductDomainValidator.validate(product));
    }
}
