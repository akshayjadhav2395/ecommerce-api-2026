package com.akshay.ecommerce.security;

import com.akshay.ecommerce.security.filter.JwtAuthenticationFilter;
import com.akshay.ecommerce.security.service.CustomUserDetailsService;
import com.akshay.ecommerce.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {

        jwtAuthenticationFilter = new JwtAuthenticationFilter(
                jwtService,
                userDetailsService
        );
    }

    /*

    When the filter asks the request for the Authorization header, pretend there isn't one.

     */

    @Test
    public void shouldContinueFilterChainWhenHeaderIsMissing() throws ServletException, IOException {

        //arrange
        when(request.getHeader("Authorization"))
                .thenReturn(null);

        //act + assert
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        //verify
        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);
    }

    /*

    Authorization header without Bearer
    Since it doesn't start with Bearer , the filter should skip JWT processing and continue the filter chain.

     */

    @Test
    public void shouldContinueFilterChainWhenAuthorizationHeaderIsNotBearer() throws ServletException, IOException {

        //arrange
        when(request.getHeader("Authorization"))
                .thenReturn("Basic abc123");

        //act + assert
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        //verify
        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(jwtService);
        verifyNoInteractions(userDetailsService);

    }

    @Test
    public void shouldAuthenticateUserWhenValidJwtIsProvided() throws ServletException, IOException {

        //arrange
        String jwt = "jwt-token";
        String username = "akshay@test.com";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + jwt);

        when(jwtService.extractUsername(jwt))
                .thenReturn(username);

        when(userDetailsService.loadUserByUsername(username))
                .thenReturn(userDetails);

        when(jwtService.isTokenValid(jwt, userDetails))
                .thenReturn(true);

        //act
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        //assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);

        assertEquals(userDetails, authentication.getPrincipal());

        //verify
        verify(jwtService).extractUsername(jwt);

        verify(userDetailsService).loadUserByUsername(username);

        verify(jwtService).isTokenValid(jwt, userDetails);

        verify(filterChain).doFilter(request, response);
    }
}
