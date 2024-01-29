package com.app.ecommerce.controllers;

import com.app.ecommerce.entity.User;
import com.app.ecommerce.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }

    @PostMapping("/createNewUser")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        User savedUser = userService.createNewUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/forAdmin")
    @PreAuthorize(("hasRole('ROLE_ADMIN')"))
//    @Secured("ROLE_ADMIN")
    public String forAdmin() {
        return "This url is only accessible to admin !!";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')" + " || hasRole('ROLE_USER')")
    @GetMapping("/forUser")
//    @Secured("ROLE_USER")
    public String forUser() {
        return "This url is only accessible to user !!";
    }
}
