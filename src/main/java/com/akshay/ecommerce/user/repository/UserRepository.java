package com.akshay.ecommerce.user.repository;

import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.entity.Role;
import com.akshay.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    String findByEmail(String email);
    List<UserResponse> findByFirstName(String firstName);
    List<UserResponse> findByLastName(String lastName);
    List<UserResponse> findByRole(Role role);
    List<UserResponse> findByFirstNameAndRole(String firstName, Role role);
}
