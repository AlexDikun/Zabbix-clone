package su.dikunia.zabbix_clone.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import su.dikunia.zabbix_clone.service.AuthenticationService;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String login, @RequestParam String password) { 
        return authService.authenticate(login, password);
    }

    @PostMapping("/refresh")
    public String refresh(@RequestParam String token) {
        return authService.refreshToken(token);
    }
}