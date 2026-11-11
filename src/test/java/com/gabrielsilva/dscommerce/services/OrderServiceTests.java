package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.dto.OrderDTO;
import com.gabrielsilva.dscommerce.entities.Order;
import com.gabrielsilva.dscommerce.entities.OrderItem;
import com.gabrielsilva.dscommerce.entities.Product;
import com.gabrielsilva.dscommerce.entities.User;
import com.gabrielsilva.dscommerce.repositories.OrderItemRepository;
import com.gabrielsilva.dscommerce.repositories.OrderRepository;
import com.gabrielsilva.dscommerce.repositories.ProductRepository;
import com.gabrielsilva.dscommerce.services.exceptions.ForbiddenException;
import com.gabrielsilva.dscommerce.services.exceptions.ResourceNotFoundException;
import com.gabrielsilva.dscommerce.tests.OrderFactory;
import com.gabrielsilva.dscommerce.tests.ProductFactory;
import com.gabrielsilva.dscommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderServices service;

    @Mock
    private OrderRepository repository;

    @Mock
    private AuthService authService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserService userService;

    private Long existingOrderId, nonExistingOrderId;
    private Long existingProductId, nonExistingProductId;
    private Order order;
    private OrderDTO orderDTO;
    private User admin, client;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingOrderId = 1L;
        nonExistingOrderId = 2L;

        existingProductId = 1L;
        nonExistingProductId = 2L;

        admin = UserFactory.createCustomAdminUser(1L, "Jef");
        client = UserFactory.createCustomClientUser(2L, "Bob");

        order = OrderFactory.createOrder(client);
        orderDTO = new OrderDTO(order);
        product = ProductFactory.createProduct();

    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged(){
        Mockito.doNothing().when(authService).validateSelfOrAdm(any());
        Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));

        OrderDTO result = service.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndSelfClientLogged(){
        Mockito.doNothing().when(authService).validateSelfOrAdm(any());
        Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));

        OrderDTO result = service.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged(){
        Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdm(any());
        Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));

        Assertions.assertThrows(ForbiddenException.class, () -> {
            OrderDTO result = service.findById(existingOrderId);
        });

    }

    @Test
    public void findByIdThrowsResourceNotFoundExceptionWhenIdDoesNotExists(){
        Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            OrderDTO result = service.findById(nonExistingOrderId);
        });

    }

    @Test
    public void insertShouldReturnOrderDTOWhenAdminLogged(){
        Mockito.when(userService.authenticated()).thenReturn(admin);
        Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);

        OrderDTO result = service.insert(orderDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldThrowsUsernameNotFoundExceptionWhenUserNotLogged() {
        Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            OrderDTO result = service.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldThrowsEntityNotFoundExceptionWhenOrderProductDoesNotExist() {
        Mockito.when(userService.authenticated()).thenReturn(client);

        product.setId(nonExistingProductId);

        Order orderTest = new Order();
        orderTest.setClient(client);

        OrderItem orderItem = new OrderItem(orderTest, product, 10.0, 2);
        orderTest.getItems().add(orderItem);

        orderDTO = new OrderDTO(orderTest);

        Mockito.when(productRepository.getReferenceById(nonExistingProductId))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> service.insert(orderDTO));

    }
}
