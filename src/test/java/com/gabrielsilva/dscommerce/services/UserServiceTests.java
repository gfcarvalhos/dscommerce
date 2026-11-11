package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.entities.User;
import com.gabrielsilva.dscommerce.projections.UserDetailsProjection;
import com.gabrielsilva.dscommerce.repositories.UserRepository;
import com.gabrielsilva.dscommerce.tests.ProductFactory;
import com.gabrielsilva.dscommerce.tests.UserDetailsFactory;
import com.gabrielsilva.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private String existingUsername, nonExistingUsername;
    private User user;
    private List<UserDetailsProjection> userDetails;

    @BeforeEach
    void setUp() throws Exception {
        existingUsername = "maria@gmail.com";
        nonExistingUsername = "user@gmail.com";

        user = UserFactory.createCustomClientUser(1L, existingUsername);

        userDetails = UserDetailsFactory.createCustomAdminUser(existingUsername);

    }

    @Test
    public void loadUserByUsernameShouldReturnUserWhenUserExists() {
        Mockito.when(repository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userDetails);

        UserDetails result = service.loadUserByUsername(existingUsername);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUsername);

    }

    @Test
    public void loadUserByUsernameShouldReturnUsernameNotFoundExceptionWhenUserDoesNotExist() {
        Mockito.when(repository.searchUserAndRolesByEmail(nonExistingUsername)).thenReturn(new ArrayList<>());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonExistingUsername);
        });

    }
}
