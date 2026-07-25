package com.akshay.ecommerce.auth.service;

import com.akshay.ecommerce.auth.dto.LoginRequest;
import com.akshay.ecommerce.auth.dto.LoginResponse;
import com.akshay.ecommerce.security.dto.CurrentUserResponse;
import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;

public interface AuthenticationService {

   public UserResponse register(UserRequest request);
   public LoginResponse login(LoginRequest request);
   public CurrentUserResponse getCurrentUser();

}
