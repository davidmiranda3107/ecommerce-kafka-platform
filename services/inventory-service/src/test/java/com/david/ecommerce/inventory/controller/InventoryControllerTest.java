package com.david.ecommerce.inventory.controller;

import com.david.ecommerce.inventory.domain.InventoryController;
import com.david.ecommerce.inventory.domain.dto.InventoryRequest;
import com.david.ecommerce.inventory.domain.dto.InventoryResponse;
import com.david.ecommerce.inventory.domain.enums.InventoryStatus;
import com.david.ecommerce.inventory.security.TestSecurityConfig;
import com.david.ecommerce.inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(InventoryController.class)
@Import(TestSecurityConfig.class)
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateInventory() throws Exception {
        InventoryRequest request = new InventoryRequest();
        request.setProductId(100L);
        request.setQuantity(10);

        InventoryResponse response = new InventoryResponse(
             100L, 10, InventoryStatus.OK
        );

        Mockito.when(inventoryService.createInventory(Mockito.any()))
                        .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
