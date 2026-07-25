package com.akshay.ecommerce.user.controller;

import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.entity.User;
import com.akshay.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping
//    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserRequest request) {
//
//        UserResponse response = userService.saveUser(request);
//
//        return new ResponseEntity<UserResponse>(response, HttpStatus.CREATED);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getUsers() {

        List<UserResponse> users = userService.getUsers();

        return new ResponseEntity<List<UserResponse>>(users, HttpStatus.OK);
    }
}
