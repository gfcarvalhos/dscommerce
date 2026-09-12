package com.gabrielsilva.dscommerce.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    @Test
    void shouldCreateUserWithCorrectData() {
        LocalDate birthDate = LocalDate.of(2000, 1, 15);

        User user = new User(
                1L,
                "Gabriel",
                "123456",
                birthDate,
                "99999999999"
        );

        assertEquals(1L, user.getId());
        assertEquals("Gabriel", user.getName());
        assertEquals("123456", user.getPassword());
        assertEquals(birthDate, user.getBirthDate());
        assertEquals("99999999999", user.getPhone());

    }

    @Test
    void shouldSetAndGetId() {
        User user = new User();

        user.setId(10L);

        assertEquals(10L, user.getId());
    }

    @Test
    void shouldSetAndGetName() {
        User user = new User();

        user.setName("Gabriel");

        assertEquals("Gabriel", user.getName());
    }

    @Test
    void shouldSetAndGetPhone() {
        User user = new User();

        user.setPhone("999999999");

        assertEquals("999999999", user.getPhone());
    }

    @Test
    void shouldSetAndGetBirthDate() {
        User user = new User();
        LocalDate birthDate = LocalDate.of(2000, 1, 15);

        user.setBirthDate(birthDate);

        assertEquals(birthDate, user.getBirthDate());
    }

    @Test
    void shouldSetAndGetPassword() {
        User user = new User();

        user.setPassword("123456");

        assertEquals("123456", user.getPassword());
    }

    @Test
    void shouldInitializeOrdersAsEmptyList() {
        User user = new User();

        assertNotNull(user.getOrders());
        assertTrue(user.getOrders().isEmpty());
    }

    @Test
    void shouldSetAndGetOrders() {
        User user = new User();

        List<Order> orders = new ArrayList<>();

        user.setOrders(orders);

        assertSame(orders, user.getOrders());
    }
}
