package com.gabrielsilva.dscommerce.services;

import com.gabrielsilva.dscommerce.entities.User;
import com.gabrielsilva.dscommerce.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserService userService;

    public void validateSelfOrAdm(long userId){
        User me = userService.autheticated();
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }
}
