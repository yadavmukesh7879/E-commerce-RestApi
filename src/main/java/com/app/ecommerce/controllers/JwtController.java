package com.app.ecommerce.controllers;

import com.app.ecommerce.entity.JwtRequest;
import com.app.ecommerce.entity.JwtResponse;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.entity.UserLoginDetails;
import com.app.ecommerce.repository.RoleRepository;
import com.app.ecommerce.repository.UserLoginDetailsRepository;
import com.app.ecommerce.repository.UserRepository;
import com.app.ecommerce.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserLoginDetailsRepository userLoginDetailsRepository;

    public JwtController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserLoginDetailsRepository userLoginDetailsRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userLoginDetailsRepository = userLoginDetailsRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createJwtToke(@RequestBody JwtRequest jwtRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getUserPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByUserName(jwtRequest.getUserName()).get();
        UserLoginDetails userLoginDetails = userLoginDetailsRepository.findById(user.getUserName()).get();
        user.setDateTime(userLoginDetails.getDateTime());
        userLoginDetailsRepository.save(new UserLoginDetails(user.getUserName(), user.getUserFirstName(), user.getUserLastName()));
        return new ResponseEntity<>(new JwtResponse(user, token), HttpStatus.OK);
    }
}
