package com.david.ecommerce.product.service;

import com.david.ecommerce.product.domain.Category;
import com.david.ecommerce.product.domain.Product;
import com.david.ecommerce.product.dto.ProductRequest;
import com.david.ecommerce.product.dto.ProductResponse;
import com.david.ecommerce.product.repository.CategoryRepository;
import com.david.ecommerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateProductSuccessfully() {
        Category category = categoryRepository.save(
                new Category(null, "Food")
        );

        ProductRequest request = new ProductRequest();
        request.setName("Milk");
        request.setDescription("Whole milk");
        request.setPrice(1.50);
        request.setSku("MLK001");
        request.setCategoryId(category.getId());

        ProductResponse product = productService.createProduct(request);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals("Milk", product.getName());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        ProductRequest request = new ProductRequest();
        request.setName("Bread");
        request.setPrice(1.0);
        request.setSku("BRD001");
        request.setCategoryId(-999L);

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> productService.createProduct(request)
        );
    }

    @Test
    void shouldFindProductById() {
        Category category = categoryRepository.save(
                new Category(null, "Food")
        );

        Product product = productRepository.save(
                Product.builder()
                        .name("Cheese")
                        .sku("CH001")
                        .category(category)
                        .price(BigDecimal.TEN)
                        .build()
        );

        ProductResponse found = productService.getProductById(product.getId());

        Assertions.assertEquals(product.getId(), found.getId());
    }
}
