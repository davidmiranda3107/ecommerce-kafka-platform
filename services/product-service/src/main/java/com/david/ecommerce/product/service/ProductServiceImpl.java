package com.david.ecommerce.product.service;

import com.david.ecommerce.product.domain.Category;
import com.david.ecommerce.product.domain.Product;
import com.david.ecommerce.product.dto.ProductRequest;
import com.david.ecommerce.product.dto.ProductResponse;
import com.david.ecommerce.product.exception.NotFoundException;
import com.david.ecommerce.product.kafka.ProductEvent;
import com.david.ecommerce.product.kafka.ProductEventProducer;
import com.david.ecommerce.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductEventProducer eventProducer;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        Category category = new Category();
        category.setId(request.getCategoryId());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(BigDecimal.valueOf(request.getPrice()))
                .sku(request.getSku())
                .category(category)
                .build();

        Product saved = productRepository.save(product);

        eventProducer.sendProductEvent(
                new ProductEvent(
                        saved.getId(),
                        saved.getName(),
                        saved.getPrice().doubleValue(),
                        product.getSku(),
                        "CREATED"
                )
        );

        return toResponse(saved);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return toResponse(findProduct(id));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProduct(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(BigDecimal.valueOf(request.getPrice()));

        Product updated = productRepository.save(product);

        eventProducer.sendProductEvent(
                new ProductEvent(
                        updated.getId(),
                        updated.getName(),
                        updated.getPrice().doubleValue(),
                        updated.getSku(),
                        "UPDATED"
                )
        );

        return toResponse(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = findProduct(id);
        productRepository.delete(product);

        eventProducer.sendProductEvent(
                new ProductEvent(
                        id,
                        null,
                        null,
                        null,
                        "DELETED"
                )
        );
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice().doubleValue(),
                product.getSku(),
                product.getCategory().getId()
        );
    }
}
