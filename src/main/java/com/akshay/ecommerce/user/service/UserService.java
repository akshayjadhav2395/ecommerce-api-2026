package com.akshay.ecommerce.user.service;

import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.entity.User;

import java.util.List;

public interface UserService {

//    public UserResponse saveUser(UserRequest request);

    public List<UserResponse> getUsers();

}
