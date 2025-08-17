package su.dikunia.zabbix_clone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import su.dikunia.zabbix_clone.security.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String login = "testUser@company.ru";
    private String password = "password"; 
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        System.setProperty("jwt.secret", "my_secret_key_here_with_at_least_256_bits");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        userDetails = org.springframework.security.core.userdetails.User
                .withUsername(login)
                .password("encoded_password") 
                .roles("USER")
                .build();
    }

    @Test
    public void testAuthenticate() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(userDetailsService.loadUserByUsername(login)).thenReturn(userDetails);
        when(jwtUtil.generateAccessToken(login)).thenReturn("generated_token");

        String token = authenticationService.authenticate(login, password);

        assertNotNull(token);
        assertEquals("generated_token", token);
    }

    @Test
    public void testRefreshToken() {
        when(jwtUtil.extractLogin(anyString())).thenReturn(login);
        when(userDetailsService.loadUserByUsername(login)).thenReturn(userDetails);
        when(jwtUtil.validateToken(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateAccessToken(login)).thenReturn("new_token");

        String newToken = authenticationService.refreshToken("old_token");

        assertNotNull(newToken);
        assertEquals("new_token", newToken);
    }
}