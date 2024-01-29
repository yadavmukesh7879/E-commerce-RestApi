package com.app.ecommerce.service.impl;

import com.app.ecommerce.entity.Role;
import com.app.ecommerce.repository.RoleRepository;
import com.app.ecommerce.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createNewRole(Role role) {
        Role savedRole = roleRepository.save(role);
        return savedRole;
    }
}
