package com.david.ecommerce.order.controller;

import com.david.ecommerce.order.config.TestSecurityConfig;
import com.david.ecommerce.order.dto.OrderItemRequest;
import com.david.ecommerce.order.dto.OrderRequest;
import com.david.ecommerce.order.dto.OrderResponse;
import com.david.ecommerce.order.security.JwtTokenProvider;
import com.david.ecommerce.order.service.OrderService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;

@WebMvcTest(OrderController.class)
@Import(TestSecurityConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setUserId(1L);

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setPrice(10d);
        itemRequest.setQuantity(1);
        itemRequest.setProductId(1L);
        request.getItems().add(itemRequest);

        OrderResponse response = new OrderResponse(
                1L, 1L, "CREATED", 10d, LocalDateTime.now(), new ArrayList<>()
        );

        Mockito.when(orderService.createOrder(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
