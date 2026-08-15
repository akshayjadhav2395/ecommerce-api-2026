package com.akshay.ecommerce.user;

import com.akshay.ecommerce.auth.dto.LoginRequest;
import com.akshay.ecommerce.common.exception.ResourceNotFoundException;
import com.akshay.ecommerce.security.service.JwtService;
import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.entity.Role;
import com.akshay.ecommerce.user.entity.User;
import com.akshay.ecommerce.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;


    @Test
    public void shouldLoadApplicationContext() {

    }

    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {

        //arrange
        UserRequest request = new UserRequest();
        request.setFirstName("Akshay");
        request.setLastName("Jadhav");
        request.setEmail("akshay@gmail.com");
        request.setPassword("akshay@123");
        request.setPhoneNumber("9320203030");
        request.setRole(Role.CUSTOMER);

        //act + assert
        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated());

        User savedUser = userRepository.
                findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

        assertNotNull(savedUser);

        assertEquals("Akshay", savedUser.getFirstName());
        assertEquals("Jadhav", savedUser.getLastName());
        assertEquals("akshay@gmail.com", savedUser.getEmail());
        assertEquals(Role.CUSTOMER, savedUser.getRole());
        assertTrue(savedUser.getActive());
    }

    @Test
    public void shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {

        //arrange
        User existingUser = User.builder()
                .firstName("Sam")
                .lastName("Jadhav")
                .email("sam@test.com")
                .password("sam@123")
                .phoneNumber("9020202020")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(existingUser);

        UserRequest request = new UserRequest();
        request.setFirstName("Sam");
        request.setLastName("Jadhav");
        request.setEmail("sam@test.com");
        request.setPassword("sam@1234");
        request.setPhoneNumber("9220202020");
        request.setRole(Role.CUSTOMER);

        //act + assert
        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldUserLoginSuccessfully() throws Exception {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akshay@best.com")
                .password(passwordEncoder.encode("akshay@123"))
                .phoneNumber("9092920000")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setEmail("akshay@best.com");
        request.setPassword("akshay@123");

        //act + arrange
        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void shouldReturnUnauthorizedWhenLoginCredentialsAreInvalid() throws Exception {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akky@test.com")
                .password(passwordEncoder.encode("akky@123"))
                .role(Role.CUSTOMER)
                .active(true)
                .phoneNumber("9090203030")
                .build();

        userRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setEmail("akky@best.com");
        request.setPassword("akshay@123");

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void shouldAllowAdminWithValidJwtGetAllUsers() throws Exception {

        //arrange

        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("aks@test.com")
                .password(passwordEncoder.encode("akky@123"))
                .role(Role.ADMIN)
                .active(true)
                .phoneNumber("9090203030")
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        //act + assert
        mockMvc.perform(

                get("/api/v1/users/all")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRestrictCustomerWithValidJwtFromGettingAllUsers() throws Exception {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akki@test.com")
                .password(passwordEncoder.encode("akky@123"))
                .role(Role.CUSTOMER)
                .active(true)
                .phoneNumber("9093203030")
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails);

        //act + assert
        mockMvc.perform(

                get("/api/v1/users/all")
                        .header("Authorization", "Bearer " + token)
        )
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
