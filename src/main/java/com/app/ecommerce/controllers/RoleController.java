package com.app.ecommerce.controllers;

import com.app.ecommerce.entity.Role;
import com.app.ecommerce.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/createNewRole")
    public ResponseEntity<Role> createNewRole(@RequestBody Role role) {
        Role savedRole = roleService.createNewRole(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }
}
