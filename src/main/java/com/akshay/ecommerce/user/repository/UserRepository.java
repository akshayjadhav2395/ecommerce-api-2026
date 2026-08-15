package com.akshay.ecommerce.user.repository;

import com.akshay.ecommerce.user.dto.UserResponse;
import com.akshay.ecommerce.user.entity.Role;
import com.akshay.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByFirstName(String firstName);
    List<User> findByLastName(String lastName);
    List<User> findByRole(Role role);
    List<User> findByFirstNameAndRole(String firstName, Role role);
}
