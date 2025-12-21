package com.david.ecommerce.product.controller;

import com.david.ecommerce.product.config.TestSecurityConfig;
import com.david.ecommerce.product.dto.CategoryRequest;
import com.david.ecommerce.product.dto.CategoryResponse;
import com.david.ecommerce.product.security.JwtTokenProvider;
import com.david.ecommerce.product.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
@Import(TestSecurityConfig.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateCategory() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Food");

        CategoryResponse response = new CategoryResponse(
                1L, "Food"
        );

        Mockito.when(categoryService.createCategory(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
