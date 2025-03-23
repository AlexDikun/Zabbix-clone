package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.repos.UserRepository;

public class UserServiceTest {

        @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_success() {
        String login = "testLogin";
        String password = "password";
        String encodedPassword = "encodedPassword";
        RoleEntity roleEntity = new RoleEntity();
        
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        
        UserEntity newUser = new UserEntity();
        newUser.setLogin(login);
        newUser.setPassword(encodedPassword);
        newUser.setRoleEntity(roleEntity);
        newUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser); // Настраиваем возврат нового пользователя

        UserEntity userEntity = userService.createUser(login, password, roleEntity);

        assertNotNull(userEntity);
        assertNotNull(userEntity.getCreatedAt());
        assertEquals(login, userEntity.getLogin());
        assertEquals(encodedPassword, userEntity.getPassword());
        assertEquals(roleEntity, userEntity.getRoleEntity());

        verify(userRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testCreateUser_userAlreadyExists() {
        String login = "existingLogin";
        RoleEntity roleEntity = new RoleEntity();

        UserEntity existingUser = new UserEntity();
        existingUser.setLogin(login);
        existingUser.setPassword("somePassword");
        existingUser.setRoleEntity(roleEntity);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(existingUser));

        UserEntity userEntity = userService.createUser(login, "newPassword", roleEntity);

        assertNotNull(userEntity);
        assertEquals(login, userEntity.getLogin());
        assertEquals(existingUser.getPassword(), userEntity.getPassword()); // Пароль не должен измениться
        assertEquals(roleEntity, userEntity.getRoleEntity());

        verify(userRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, never()).encode(any(String.class)); // encode не должен вызываться
        verify(userRepository, never()).save(any(UserEntity.class)); // save не должен вызываться
    }
}
    
