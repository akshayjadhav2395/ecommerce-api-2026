package com.akshay.ecommerce.user.controller;

import com.akshay.ecommerce.EcommerceApiApplication;
import com.akshay.ecommerce.security.filter.JwtAuthenticationFilter;
import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.security.sasl.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        JwtAuthenticationFilter.class,
                        EcommerceApiApplication.class
                }
        )
)
@EnableMethodSecurity
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "akshay@test.com", roles = "ADMIN")
    public void shouldAllowAdminToGetAllUsers() throws Exception {

        //arrange
        List<UserResponse> userResponseList = new ArrayList<>();

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFirstName("Akshay");
        userResponse.setLastName("Jadhav");
        userResponse.setEmail("akshay@test.com");
        userResponse.setPhoneNumber("9494000000");

        userResponseList.add(userResponse);

        when(userService.getUsers())
                .thenReturn(userResponseList);

        //act + assert
        mockMvc.perform(
                get("/api/v1/users/all")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Akshay"))
                .andExpect(jsonPath("$[0].lastName").value("Jadhav"))
                .andExpect(jsonPath("$[0].email").value("akshay@test.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("9494000000"));
    }

    @Test
    @WithMockUser(username = "akshay@java.com", roles = "CUSTOMER")
    public void shouldRestrictCustomerToGetAllUsers() throws Exception {

        //act + assert
        mockMvc.perform(
                get("/api/v1/users/all")
        )
                .andExpect(status().isForbidden());

        //verify
        verify(userService, never()).getUsers();
    }

    @Test
    public void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {

        mockMvc.perform(
                get("/api/v1/users/all")
        )
                .andExpect(status().isUnauthorized());

        //verify
        verify(userService, never()).getUsers();
    }
}