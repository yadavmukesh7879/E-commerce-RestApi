package com.app.ecommerce.service.impl;

import com.app.ecommerce.entity.Role;
import com.app.ecommerce.entity.UserLoginDetails;
import com.app.ecommerce.repository.UserLoginDetailsRepository;
import com.app.ecommerce.service.UserService;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.repository.RoleRepository;
import com.app.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserLoginDetailsRepository userLoginDetailsRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserLoginDetailsRepository userLoginDetailsRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userLoginDetailsRepository = userLoginDetailsRepository;
    }

    @Override
    public User createNewUser(User user) {
        Role role = roleRepository.findById("ROLE_USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        UserLoginDetails userLoginDetails = new UserLoginDetails(user.getUserName(), user.getUserFirstName(), user.getUserLastName());
        userLoginDetailsRepository.save(userLoginDetails);
        User saved = userRepository.save(user);
        saved.setDateTime(userLoginDetails.getDateTime());
        return saved;
    }

    public void initRoleAndUser() {
        Role adminRole = new Role();

        adminRole.setName("ROLE_ADMIN");
        adminRole.setDescription("Admin role it is only for admin");
        roleRepository.save(adminRole);

        Role userRole = new Role();

        userRole.setName("ROLE_USER");
        userRole.setDescription("Default role for new user");

        roleRepository.save(userRole);

        User adminUser = new User();

        adminUser.setUserName("admin@123");
        adminUser.setUserFirstName("Krishna");
        adminUser.setUserLastName("Yadav");
        adminUser.setUserPassword(passwordEncoder.encode("admin@123"));

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles);
        userRepository.save(adminUser);


//        User user = new User();
//        user.setUserName("mukesh@7879");
//        user.setUserFirstName("Mukesh");
//        user.setUserLastName("Yadav");
//        user.setUserPassword(passwordEncoder.encode("mukesh@7879"));
//
//        Set<Role> userRoles = new HashSet<>();
//        userRoles.add(userRole);
//        user.setRoles(userRoles);
//        userRepository.save(user);

    }
}
