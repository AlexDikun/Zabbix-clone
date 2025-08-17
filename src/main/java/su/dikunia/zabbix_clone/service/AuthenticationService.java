package su.dikunia.zabbix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import su.dikunia.zabbix_clone.security.JwtUtil;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String authenticate(String login, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        return jwtUtil.generateAccessToken(userDetails.getUsername());
    }

    public String refreshToken(String token) {
        final String login = jwtUtil.extractLogin(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        if (jwtUtil.validateToken(token, userDetails.getUsername())) {
            return jwtUtil.generateAccessToken(userDetails.getUsername());
        }
        return null;
    }
}