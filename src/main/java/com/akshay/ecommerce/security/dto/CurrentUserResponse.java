package com.akshay.ecommerce.security.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CurrentUserResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String role;


    public CurrentUserResponse(Long id, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
