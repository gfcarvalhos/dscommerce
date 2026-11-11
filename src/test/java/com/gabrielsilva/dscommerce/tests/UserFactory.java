package com.gabrielsilva.dscommerce.tests;

import com.gabrielsilva.dscommerce.entities.Category;
import com.gabrielsilva.dscommerce.entities.Role;
import com.gabrielsilva.dscommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {
    public static User createClientUser() {
        User user = new User(1L, "Maria",
                "$2a$10$3EjGJGDOuhRpIj2rBLuaTelnaS.yJoK9IQLojdQ3lje/sqfH3uj.a",
                LocalDate.parse("2001-07-25"), "988888888", "maria@gmail.com");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser() {
        User user = new User(1L, "Alex",
                "$2a$10$3EjGJGDOuhRpIj2rBLuaTelnaS.yJoK9IQLojdQ3lje/sqfH3uj.a",
                LocalDate.parse("2000-07-25"), "988888887", "alex@gmail.com");
        user.addRole(new Role(1L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username){
        User user = new User(id, "Alex",
                "$2a$10$3EjGJGDOuhRpIj2rBLuaTelnaS.yJoK9IQLojdQ3lje/sqfH3uj.a",
                LocalDate.parse("2000-07-25"), "988888887", username);
        user.addRole(new Role(1L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username){
        User user = new User(id, "Maria",
                "$2a$10$3EjGJGDOuhRpIj2rBLuaTelnaS.yJoK9IQLojdQ3lje/sqfH3uj.a",
                LocalDate.parse("2001-07-25"), "988888888", username);
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

}
