package com.akshay.ecommerce.user.repository;

import com.akshay.ecommerce.user.entity.Role;
import com.akshay.ecommerce.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnTrueWhenEmailExist() {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akshay@test.com")
                .password("akshay@123")
                .phoneNumber("9039030300")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        //act
        boolean exists = userRepository.existsByEmail(user.getEmail());

        //assert
        assertTrue(exists);

    }

    @Test
    public void shouldReturnFalseWhenEmailDoesNotExist() {

        //act
        boolean exists = userRepository.existsByEmail("akshaya@test.com");

        //assert
        assertFalse(exists);

    }

    @Test
    public void shouldReturnTrueWhenEmailExists() {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akshay@test.com")
                .password("akshay@123")
                .phoneNumber("9039030300")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        //act
        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());

        //assert
        assertTrue(byEmail.isPresent());

        User userByEmail = byEmail.get();

        assertNotNull(userByEmail);

        assertEquals("akshay@test.com", userByEmail.getEmail());

        assertEquals("Akshay", userByEmail.getFirstName());

        assertEquals("Jadhav", userByEmail.getLastName());
    }

    @Test
    public void shouldReturnEmptyWhenEmailDoesNotExist() {

        Optional<User> byEmail = userRepository.findByEmail("akshay@test.com");

        //assert
        assertTrue(byEmail.isEmpty());
    }

    @Test
    public void shouldFindUserByFirstName() {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akshay@test.com")
                .password("akshay@123")
                .phoneNumber("9039030300")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        //act
        List<User> users = userRepository.findByFirstName(user.getFirstName());

        //assert
        assertFalse(users.isEmpty());

        assertEquals(1, users.size());

        assertEquals("Akshay", users.get(0).getFirstName());

        assertEquals("Jadhav", users.get(0).getLastName());
    }

    @Test
    public void shouldReturnEmptyWhenFirstNameDoesNotExist() {

        List<User> users = userRepository.findByFirstName("Akshay");

        assertTrue(users.isEmpty());
    }

    @Test
    public void shouldFindUserByLastName() {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akshay@test.com")
                .password("akshay@123")
                .phoneNumber("9039030300")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        //act
        List<User> users = userRepository.findByLastName(user.getLastName());

        //assert
        assertNotNull(users);

        assertEquals(1, users.size());

        assertEquals("Jadhav", users.get(0).getLastName());
    }

    @Test
    public void shouldReturnEmptyWhenLastNameDoesNotExist() {

        List<User> users = userRepository.findByLastName("akshay@test.com");

        assertTrue(users.isEmpty());
    }

    @Test
    public void shouldFindUserByRole() {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akshay@test.com")
                .password("akshay@123")
                .phoneNumber("9039030300")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        //act
        List<User> users = userRepository.findByRole(user.getRole());

        //assert
        assertFalse(users.isEmpty());

        assertEquals(1, users.size());

        assertEquals(Role.CUSTOMER, users.get(0).getRole());
    }

    @Test
    public void shouldReturnEmptyWhenUserDoesNotExist() {

        List<User> users = userRepository.findByRole(Role.CUSTOMER);
        assertTrue(users.isEmpty());
    }

    @Test
    public void shouldFindUserByFirstNameAndRole() {

        //arrange
        User user = User.builder()
                .firstName("Akshay")
                .lastName("Jadhav")
                .email("akshay@test.com")
                .password("akshay@123")
                .phoneNumber("9039030300")
                .role(Role.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        //act
        List<User> users = userRepository.findByFirstNameAndRole(user.getFirstName(), user.getRole());

        //assert
        assertFalse(users.isEmpty());

        assertEquals(1, users.size());

        assertEquals("Akshay", users.get(0).getFirstName());
        assertEquals(Role.CUSTOMER, users.get(0).getRole());
    }

    @Test
    public void shouldReturnEmptyWhenUserByFirstNameAndRoleDoesNotExist() {

        List<User> users = userRepository.findByFirstNameAndRole("akshay@test.com", Role.CUSTOMER);

        assertTrue(users.isEmpty());

    }
}
