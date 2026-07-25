package com.akshay.ecommerce.auth.controller;

import com.akshay.ecommerce.auth.dto.LoginRequest;
import com.akshay.ecommerce.auth.dto.LoginResponse;
import com.akshay.ecommerce.auth.service.AuthenticationService;
import com.akshay.ecommerce.common.exception.ApiResponse;
import com.akshay.ecommerce.security.dto.CurrentUserResponse;
import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserRequest request) {

        UserResponse response = authenticationService.register(request);

        return new ResponseEntity<UserResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = this.authenticationService.login(loginRequest);

        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CurrentUserResponse>> getCurrentUser() {

        CurrentUserResponse currentUser = authenticationService.getCurrentUser();

        ApiResponse apiResponse = new ApiResponse(true, "Current User Fetched Successfully!", currentUser);

        return ResponseEntity.ok(apiResponse);
    }
}
