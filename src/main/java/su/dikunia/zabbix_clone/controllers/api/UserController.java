package su.dikunia.zabbix_clone.controllers.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        System.out.println("Админ создает профиль пользователю!");

        UserDTO createdUser = userService.createUser(userDTO, Optional.empty());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); 
    }
    
}
