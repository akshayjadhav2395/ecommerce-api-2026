package com.akshay.ecommerce.auth.controller;
import com.akshay.ecommerce.EcommerceApiApplication;
import com.akshay.ecommerce.auth.dto.LoginRequest;
import com.akshay.ecommerce.auth.dto.LoginResponse;
import com.akshay.ecommerce.auth.service.AuthenticationService;
import com.akshay.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.akshay.ecommerce.security.dto.CurrentUserResponse;
import com.akshay.ecommerce.security.filter.JwtAuthenticationFilter;
import com.akshay.ecommerce.security.service.JwtService;
import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        JwtAuthenticationFilter.class,
                        EcommerceApiApplication.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserDetails userDetails;

    @MockitoBean
    private JwtService jwtService;

    /* Register User Test */

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {

        //arrange
        UserRequest request = new UserRequest();
        request.setFirstName("Akshay");
        request.setLastName("Jadhav");
        request.setEmail("akshay@test.com");
        request.setPassword("akshay@123");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setFirstName("Akshay");
        response.setLastName("Jadhav");
        response.setEmail("akshay@test.com");

        when(authenticationService.register(any(UserRequest.class)))
                .thenReturn(response);

        //Act + assert
        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Akshay"))
                .andExpect(jsonPath("$.lastName").value("Jadhav"))
                .andExpect(jsonPath("$.email").value("akshay@test.com"));

        //verify
        verify(authenticationService).register(any(UserRequest.class));

    }

    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExist() throws Exception {

        //arrange
        UserRequest request = new UserRequest();
        request.setFirstName("Akshay");
        request.setLastName("Jadhav");
        request.setEmail("akshay@test.com");
        request.setPassword("akshay@123");

        when(authenticationService.register(any(UserRequest.class)))
                .thenThrow(new ResourceAlreadyExistsException("User already exist"));

        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isConflict());

        //verify
        verify(authenticationService).register(any(UserRequest.class));
    }

    @Test
    public void shouldReturnBadRequestWhenRegistrationRequestIsInvalid() throws Exception {

        //arrange
        UserRequest request = new UserRequest();
        request.setFirstName("Akshay");
        request.setLastName("Jadhav");
        request.setEmail("");
        request.setPassword("akshay@123");

        //act + assert
        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest());

        //verify
        verify(authenticationService, never()).register(any(UserRequest.class));
    }

    /* Login User Test */

    @Test
    public void shouldUserLoginSuccessfully() throws Exception {

        //arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("akshay@test.com");
        request.setPassword("akshay@123");


        LoginResponse loginResponse = new LoginResponse("jwt-token");

        when(authenticationService.login(any(LoginRequest.class)))
                .thenReturn(loginResponse);

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        //verify
        verify(authenticationService).login(any(LoginRequest.class));
    }

    @Test
    public void shouldThrowBadCredentialsException() throws Exception {

        //arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("akshay@test.com");
        request.setPassword("123");

        when(authenticationService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        //act + assert
        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isUnauthorized());

        //verify
        verify(authenticationService).login(any(LoginRequest.class));
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidLoginRequest() throws Exception {

        //arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("akshay.com");
        request.setPassword("akshay@123");

        //act + arrange
        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest());

        //verify
        verify(authenticationService, never()).login(request);
    }

    /* Current user Test */

    @Test
    public void shouldGetCurrentUserSuccessfully() throws Exception {

        //arrange
        CurrentUserResponse currentUserResponse = new CurrentUserResponse(
                1L,
                "Akshay",
                "Jadhav",
                "akshay@test.com",
                "CUSTOMER"
        );

        when(authenticationService.getCurrentUser())
                .thenReturn(currentUserResponse);

        //act + assert
        mockMvc.perform(
                        get("/api/auth/me")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Current User Fetched Successfully!"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.firstName").value("Akshay"))
                .andExpect(jsonPath("$.data.lastName").value("Jadhav"))
                .andExpect(jsonPath("$.data.email").value("akshay@test.com"))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
    }

}
