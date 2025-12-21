package com.david.ecommerce.product.controller;

import com.david.ecommerce.product.config.TestSecurityConfig;
import com.david.ecommerce.product.dto.ProductRequest;
import com.david.ecommerce.product.dto.ProductResponse;
import com.david.ecommerce.product.security.JwtTokenProvider;
import com.david.ecommerce.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ProductController.class)
@Import(TestSecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Milk");
        request.setDescription("Milk");
        request.setPrice(1.50);
        request.setSku("MLK001");
        request.setCategoryId(1L);

        ProductResponse response = new ProductResponse(
                1L, "Milk", "Milk", 1.50, "MLK001", 1L
        );

        Mockito.when(productService.createProduct(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
