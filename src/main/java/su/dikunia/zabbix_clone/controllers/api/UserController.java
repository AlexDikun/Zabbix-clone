package su.dikunia.zabbix_clone.controllers.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.enums.RoleName;
import su.dikunia.zabbix_clone.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        System.out.println("Админ создает профиль пользователю!");

        UserDTO createdUser = userService.createUser(userDTO, Optional.empty());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED); 
    }

    @PatchMapping("/{user_id}/change-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> changeUserRole(@PathVariable Long user_id, @RequestParam RoleName roleName) {
        System.out.println("Админ меняет пользователю роль");

        UserDTO updatedUser = userService.changeUserRole(user_id, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{user_id}/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeUserPassword(@PathVariable Long user_id, @RequestParam String newPassword) {
        System.out.println("Админ меняет пользователю пароль!");

        userService.changeUserPassword(user_id, newPassword);
        return ResponseEntity.ok("Пользователю выдан новый пароль!");
    }

    @DeleteMapping("/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> archiveUser(@RequestParam String login, @RequestParam(defaultValue = "30") int retentionDays) {
        System.out.println("Админ удаляет учетную запись пользователя!");

        try {
            userService.archiveUser(login, retentionDays);
            return ResponseEntity.ok("Учатная запись " + login + "была удалена!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
