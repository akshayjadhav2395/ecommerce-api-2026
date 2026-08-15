package com.akshay.ecommerce.auth.service;

import com.akshay.ecommerce.auth.dto.LoginRequest;
import com.akshay.ecommerce.auth.dto.LoginResponse;
import com.akshay.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.akshay.ecommerce.security.service.JwtService;
import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.entity.User;
import com.akshay.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private UserRequest request;

    @BeforeEach
    public void setUp() {

        request = new UserRequest();
        request.setFirstName("Akshay");
        request.setLastName("Jadhav");
        request.setEmail("akshay@test.com");
        request.setPassword("akshay@java");

    }

    @Test
    public void shouldRegisterUserSuccessfully() {

        //arrange
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");


        User user = new User();
        user.setId(1L);
        user.setFirstName("Akshay");
        user.setLastName("Jadhav");
        user.setEmail("akshay@test.com");
        user.setPassword("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);


        // argumentCaptor will capture actual user object
        ArgumentCaptor<User> userAC = ArgumentCaptor.forClass(User.class);

        //act
        UserResponse userResponse = authenticationService.register(request);

        //assert
        assertNotNull(userResponse);

        assertEquals(1L, userResponse.getId());
        assertEquals("Akshay", userResponse.getFirstName());
        assertEquals("Jadhav", userResponse.getLastName());
        assertEquals("akshay@test.com", userResponse.getEmail());

        //verify
        verify(userRepository).existsByEmail(request.getEmail());

        verify(passwordEncoder).encode(request.getPassword());

        verify(userRepository).save(userAC.capture());

        // assert captured user

        User capturedUser = userAC.getValue();

        assertEquals("Akshay", capturedUser.getFirstName());
        assertEquals("Jadhav", capturedUser.getLastName());
        assertEquals("akshay@test.com", capturedUser.getEmail());

        // verify encoded password
        assertEquals("encodedPassword", capturedUser.getPassword());

    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() {

        //arrange
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        //act + assert
        ResourceAlreadyExistsException exception =
                assertThrows(ResourceAlreadyExistsException.class,
                () -> authenticationService.register(request));

        assertEquals("User Already Exists!", exception.getMessage());

        //verify
        verify(userRepository).existsByEmail(request.getEmail());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    public void shouldThrowExceptionWhenRepositoryFails() {

        //arrange
        when(userRepository.existsByEmail(request.getEmail()))
                .thenThrow(new RuntimeException("Database error!"));

        //act + assert
        RuntimeException runtimeException =
                assertThrows(
                        RuntimeException.class,
                        () -> authenticationService.register(request));

        assertEquals("Database error!", runtimeException.getMessage());

        //verify
        verify(userRepository).existsByEmail(request.getEmail());

        verify(passwordEncoder, never()).encode(anyString());

        verify(userRepository, never()).save(any(User.class));
    }

    /*  Login Test */

    @Test
    public void shouldLoginSuccessfully() {

        //arrange

        LoginRequest request = new LoginRequest();
        request.setEmail("akshay@test.com");
        request.setPassword("akshay@123");

        when(userDetailsService.loadUserByUsername(request.getEmail()))
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails))
                .thenReturn("jwt-token");

        //act
        LoginResponse loginResponse = authenticationService.login(request);

        //assert
        assertNotNull(loginResponse);

        assertEquals("jwt-token", loginResponse.getToken());

        //verify
        ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(authenticationManager).authenticate(authCaptor.capture());

        verify(userDetailsService).loadUserByUsername(request.getEmail());

        verify(jwtService).generateToken(userDetails);

        //assert argument captor
        UsernamePasswordAuthenticationToken authCaptorValue = authCaptor.getValue();

        assertEquals(request.getEmail(), authCaptorValue.getPrincipal());

        assertEquals(request.getPassword(), authCaptorValue.getCredentials());

    }

    @Test
    public void shouldThrowExceptionWhenLoginFailed() {

        //arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("akshay@test.com");
        request.setPassword("akshay@123");

        when(authenticationManager.authenticate(any()))
                .thenThrow(
                        new BadCredentialsException("Invalid credentials")
                );

        //assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authenticationService.login(request));

        assertEquals("Invalid credentials", exception.getMessage());

        //verify
        verify(authenticationManager)
                .authenticate(
                        any(UsernamePasswordAuthenticationToken.class)
        );

        verify(userDetailsService, never()).loadUserByUsername(request.getEmail());

        verify(jwtService, never()).generateToken(any(UserDetails.class));

    }

}

