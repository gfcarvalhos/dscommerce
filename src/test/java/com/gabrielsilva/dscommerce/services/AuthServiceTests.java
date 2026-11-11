package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.entities.User;
import com.gabrielsilva.dscommerce.services.exceptions.ForbiddenException;
import com.gabrielsilva.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserService userService;

    private User admin, selfClient, otherClient;

    @BeforeEach
    void setUp() throws Exception {
        admin = UserFactory.createAdminUser();
        selfClient = UserFactory.createCustomClientUser(2L, "Bob");
        otherClient = UserFactory.createCustomClientUser(3L, "Ana");
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenAdminLogged(){
        Mockito.when(userService.authenticated()).thenReturn(admin);

        Assertions.assertDoesNotThrow(() -> {
            service.validateSelfOrAdm(admin.getId());
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenSelfLogged() {
        Mockito.when(userService.authenticated()).thenReturn(selfClient);

        Assertions.assertDoesNotThrow(() -> {
            service.validateSelfOrAdm(selfClient.getId());
        });
    }

    @Test
    public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenClientOtherLogged() {
        Mockito.when(userService.authenticated()).thenReturn(selfClient);

        Assertions.assertThrows(ForbiddenException.class, () -> {
            service.validateSelfOrAdm(otherClient.getId());
        });
    }
}
