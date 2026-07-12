package com.akshay.ecommerce.user.service;

import com.akshay.ecommerce.user.dto.UserRequest;
import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.entity.User;
import com.akshay.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.akshay.ecommerce.user.mapper.UserMapper;
import com.akshay.ecommerce.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse saveUser(UserRequest request) {

        User user = UserMapper.toEntity(request);

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }
        else {
            User savedUser = userRepository.save(user);

            return UserMapper.toResponse(userRepository.save(savedUser));
        }
    }
}
