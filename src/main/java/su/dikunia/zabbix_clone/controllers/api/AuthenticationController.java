package su.dikunia.zabbix_clone.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import su.dikunia.zabbix_clone.dto.AuthDTO;
import su.dikunia.zabbix_clone.service.AuthenticationService;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthDTO authDto) { 
        return authService.authenticate(authDto.getLogin(), authDto.getPassword());
    }

    @PostMapping("/refresh")
    public String refresh(@RequestParam String token) {
        return authService.refreshToken(token);
    }
}