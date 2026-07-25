package com.akshay.ecommerce.security.util;

import com.akshay.ecommerce.security.principal.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {

    }

    public static CustomUserPrincipal getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (CustomUserPrincipal) authentication.getPrincipal();
    }

}
