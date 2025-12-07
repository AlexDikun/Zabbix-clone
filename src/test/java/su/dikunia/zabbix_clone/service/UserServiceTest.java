package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.enums.RoleName;
import su.dikunia.zabbix_clone.repos.RoleRepository;
import su.dikunia.zabbix_clone.repos.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

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
        RoleName roleName = RoleName.STAFF;   

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        UserEntity newUser = new UserEntity();
        newUser.setId(1L);
        newUser.setLogin(login);
        newUser.setPassword(encodedPassword);
        newUser.setRoleEntity(roleEntity);
        newUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.save(any(UserEntity.class))).thenReturn(newUser);

        UserDTO userDTO = userService.createUser(new UserDTO(login, password), Optional.of(roleEntity));

        assertNotNull(userDTO);
        assertNotNull(userDTO.getId());
        assertEquals(login, userDTO.getLogin());

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

        UserDTO userDTO = userService.createUser(new UserDTO(login, "newPassword"), Optional.of(roleEntity));

        assertNotNull(userDTO);
        assertEquals(login, userDTO.getLogin());

        verify(userRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, never()).encode(any(String.class));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testChangeUserRole_succes() {
        String login = "login@company.su";
        String password = "password"; 
        RoleName oldRole = RoleName.STAFF;  
        
        RoleEntity roleSTAFF = new RoleEntity();
        roleSTAFF.setName(oldRole);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(124L);
        userEntity.setLogin(login);
        userEntity.setPassword(password);
        userEntity.setRoleEntity(roleSTAFF);
        userEntity.setUpdatedAt(LocalDateTime.now().minusHours(1));

        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        RoleName newRole = RoleName.MODER;
        RoleEntity roleMODER = new RoleEntity();
        roleMODER.setName(newRole);

        when(roleRepository.findByName(newRole)).thenReturn(Optional.of(roleMODER));

        LocalDateTime oldUpdatedAt = userEntity.getUpdatedAt();
        assertNotNull(oldUpdatedAt, "oldUpdatedAt не должно быть null");

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedEntity = invocation.getArgument(0);
            savedEntity.setUpdatedAt(LocalDateTime.now()); 
            return savedEntity;
        });

        UserDTO userDTO = userService.changeUserRole(userEntity.getId(), newRole);

        assertNotNull(userDTO);
        assertEquals(login, userDTO.getLogin());
        assertEquals(newRole, userDTO.getRoleName());
        assertNotEquals(oldUpdatedAt, userEntity.getUpdatedAt());

        assertTrue(userEntity.getUpdatedAt().isAfter(oldUpdatedAt));

        InOrder inOrder = inOrder(userRepository, roleRepository);
        inOrder.verify(userRepository).findById(124L);
        inOrder.verify(roleRepository).findByName(newRole);
        inOrder.verify(userRepository).save(userEntity);

        verify(userRepository, never()).delete(any());
        verify(roleRepository, never()).save(any());
    }

    @Test
    public void testChangeUserRole_roleNotFound() {
        Long userId = 124L;
        RoleName newRole = RoleName.MODER;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setLogin("login@company.su");
        userEntity.setPassword("password");
        userEntity.setRoleEntity(new RoleEntity());

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(roleRepository.findByName(newRole)).thenReturn(Optional.empty());

        assertThrows(
            EntityNotFoundException.class,
            () -> userService.changeUserRole(userId, newRole)
        );
    }

    public void testChangeUserPassword_succes() {
        Long userId = 122L;
        String oldPassword = "oldPassword";
        String newPassword = "newPassword123";
        String encodedNewPassword = "encodedNewPassword";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setLogin("login@company.su");
        userEntity.setPassword(oldPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.changeUserPassword(userId, newPassword);

        assertEquals(encodedNewPassword, userEntity.getPassword(), "Пароль должен быть закодирован и обновлён");

        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(userEntity);
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void testChangeUserPassword_userNotFound() {
        Long userId = 999L;
        String newPassword = "newPassword123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(
            EntityNotFoundException.class,
            () -> userService.changeUserPassword(userId, newPassword)
        );
    }

    @Test
    void archiveUser_shouldSetDeletedAndDeletedAt() {
        String login = "login@company.su";
        int retentionDays = 30;

        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);
        userEntity.setDeleted(false);
        userEntity.setDeletedAt(null);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(userEntity));
        userService.archiveUser(login, retentionDays);

        assertTrue(userEntity.isDeleted());
        assertNotNull(userEntity.getDeletedAt());
        assertEquals(LocalDateTime.now().plusDays(retentionDays).toLocalDate(), userEntity.getDeletedAt().toLocalDate());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void archiveUser_shouldThrowExceptionIfUserNotFound() {
        String login = "nonExistentUser";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.archiveUser(login, 30);
        });
    }
}
    
