package com.gabrielsilva.dscommerce.controllers;

import com.gabrielsilva.dscommerce.dto.OrderDTO;
import com.gabrielsilva.dscommerce.entities.Order;
import com.gabrielsilva.dscommerce.entities.User;
import com.gabrielsilva.dscommerce.tests.OrderFactory;
import com.gabrielsilva.dscommerce.tests.ProductFactory;
import com.gabrielsilva.dscommerce.tests.TokenUtil;
import com.gabrielsilva.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String productName;
    private String adminToken, clientToken, invalidToken;
    private Long existingId, nonExistingId;

    @BeforeEach
    void setUp() throws Exception{

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";

        productName = "Macbook";
        existingId = 1L;
        nonExistingId = 100L;

        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "xpto"; //simula token invalido

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenAdminLoggedAndIdExists() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{existingId}", existingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId));
        result.andExpect(jsonPath("$.client").exists());
        result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenClientLoggedAndIdExistsAndItsFromClient() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{existingId}", existingId*3)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(existingId*3));
        result.andExpect(jsonPath("$.client").exists());
        result.andExpect(jsonPath("$.client.name").value("Maria Brown"));
    }

    @Test
    public void findByIdShouldReturnForbiddenWhenClientLoggedAndIdExistsAndItsNotFromClient() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{existingId}", existingId*2)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenAdminLoggedAndIdDoesNotExist() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{existingId}", nonExistingId)
                        .header("Authorization", "Bearer " + adminToken)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenClientLoggedAndIdDoesNotExist() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{existingId}", nonExistingId)
                        .header("Authorization", "Bearer " + clientToken)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isNotFound());

    }

    @Test
    public void findByIdShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

        ResultActions result = mockMvc
                .perform(get("/orders/{existingId}", existingId)
                        .header("Authorization", "Bearer " + invalidToken)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isUnauthorized());

    }
}
