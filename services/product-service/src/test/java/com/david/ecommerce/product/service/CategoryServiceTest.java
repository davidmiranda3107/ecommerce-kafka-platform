package com.david.ecommerce.product.service;

import com.david.ecommerce.product.domain.Category;
import com.david.ecommerce.product.dto.CategoryRequest;
import com.david.ecommerce.product.dto.CategoryResponse;
import com.david.ecommerce.product.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateCategorySuccessfully() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Food");

        CategoryResponse category = categoryService.createCategory(request);

        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals("Food", category.getName());
    }

    @Test
    void shouldFindCategoryById() {
        Category category = categoryRepository.save(new Category(null,"Food"));

        CategoryResponse found = categoryService.getCategoryById(category.getId());

        Assertions.assertEquals(category.getId(), found.getId());

    }
}
