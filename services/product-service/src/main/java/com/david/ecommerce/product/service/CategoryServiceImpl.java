package com.david.ecommerce.product.service;

import com.david.ecommerce.product.domain.Category;
import com.david.ecommerce.product.dto.CategoryRequest;
import com.david.ecommerce.product.dto.CategoryResponse;
import com.david.ecommerce.product.exception.NotFoundException;
import com.david.ecommerce.product.kafka.CategoryEvent;
import com.david.ecommerce.product.kafka.CategoryEventProducer;
import com.david.ecommerce.product.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryEventProducer eventProducer;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category saved = categoryRepository.save(category);

        eventProducer.sendCategoryEvent(
                new CategoryEvent(saved.getId(), saved.getName(), "CREATED")
        );

        return toResponse(saved);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return toResponse(findCategory(id));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);

        category.setName(request.getName());

        Category updated = categoryRepository.save(category);

        eventProducer.sendCategoryEvent(
                new CategoryEvent(
                        updated.getId(), updated.getName(), "UPDATED"
                )
        );

        return toResponse(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = findCategory(id);
        categoryRepository.delete(category);

        eventProducer.sendCategoryEvent(
                new CategoryEvent(
                        id, null, "DELETED"
                )
        );

    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(), category.getName()
        );
    }
}
