package com.app.ecommerce.service;

import com.app.ecommerce.entity.User;

public interface UserService {
    public User createNewUser(User user);

    public void initRoleAndUser();
}
