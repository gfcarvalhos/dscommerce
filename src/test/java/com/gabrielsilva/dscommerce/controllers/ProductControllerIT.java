package com.gabrielsilva.dscommerce.controllers;

import com.gabrielsilva.dscommerce.dto.ProductDTO;
import com.gabrielsilva.dscommerce.entities.Product;
import com.gabrielsilva.dscommerce.tests.ProductFactory;
import com.gabrielsilva.dscommerce.tests.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String clientUsername, clientPassword, adminUsername, adminPassword;
    private String productName;
    private String adminToken, clientToken, invalidToken;

    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    void setUp() throws Exception{

        clientUsername = "maria@gmail.com";
        clientPassword = "123456";
        adminUsername = "alex@gmail.com";
        adminPassword = "123456";

        productName = "Macbook";

        adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
        clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
        invalidToken = adminToken + "xpto"; //simula token invalido

        productDTO = ProductFactory.createProductDTO();
        product = ProductFactory.createProduct();
    }

    @Test
    public void findAllShouldReturnPageWhenNameParamIsNotEmpty()  throws Exception {

        ResultActions result = mockMvc
                .perform(get("/products?name={productName}", productName)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(3L));
        result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    }

    @Test
    public void findAllShouldReturnPageWhenNameParamIsEmpty()  throws Exception {

        ResultActions result = mockMvc
                .perform(get("/products")
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0].id").value(1L));
        result.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
    }

    @Test
    public void insertShouldReturnProductDTOCreatedWhenAdminLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").value(26L));
        result.andExpect(jsonPath("$.name").value("Google"));
        result.andExpect(jsonPath("$.categories[0].id").value(1L));
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidName() throws Exception {

        product.setName("AB");
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isUnprocessableContent());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndInvalidDescription() throws Exception {

        product.setDescription("AB");
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isUnprocessableContent());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsNegative() throws Exception {

        product.setPrice(-20.0);
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isUnprocessableContent());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndPriceIsZero() throws Exception {

        product.setPrice(0.0);
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isUnprocessableContent());
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndProductHasNotCategory() throws Exception {

        product.getCategories().clear();
        productDTO = new ProductDTO(product);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc
                .perform(post("/products")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(jsonBody)
                        .accept(String.valueOf(MediaType.APPLICATION_JSON)));

        result.andExpect(status().isUnprocessableContent());
    }
}
