package com.akshay.ecommerce.auth.service;

import com.akshay.ecommerce.auth.dto.LoginRequest;
import com.akshay.ecommerce.auth.dto.LoginResponse;
import com.akshay.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.akshay.ecommerce.security.dto.CurrentUserResponse;
import com.akshay.ecommerce.security.principal.CustomUserPrincipal;
import com.akshay.ecommerce.security.service.JwtService;
import com.akshay.ecommerce.security.util.SecurityUtils;
import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.entity.User;
import com.akshay.ecommerce.user.mapper.UserMapper;
import com.akshay.ecommerce.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse register(UserRequest request) {

        User user = UserMapper.toEntity(request);

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User Already Exists!");
        }
        else {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            User savedUser = userRepository.save(user);

            return UserMapper.toResponse(savedUser);
        }
    }

    public LoginResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);
    }

    @Override
    public CurrentUserResponse getCurrentUser() {

        CustomUserPrincipal currentUser = SecurityUtils.getCurrentUser();

        return new CurrentUserResponse(
                currentUser.getId(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getEmail(),
                currentUser.getRole().name()
        );
    }

}
