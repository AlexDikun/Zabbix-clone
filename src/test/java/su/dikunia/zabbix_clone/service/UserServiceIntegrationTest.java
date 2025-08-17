package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.config.SecurityConfiguration;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.repos.RoleRepository;
import su.dikunia.zabbix_clone.repos.UserRepository;

@SpringBootTest@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(SecurityConfiguration.class) 
@Transactional
@Rollback 
public class UserServiceIntegrationTest {    
    static {        
        System.setProperty("jwt.secret", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.e30.QwoOdMKxwpjCuC14w47CisOQw74wD8OKw6p7cmjDpsKnHcOfw4lKHcKLZyNjOQ");    
    }    
    
    @Autowired    
    private UserService userService;    
    
    @Autowired    
    private RoleRepository roleRepository;    
    
    @Autowired    
    private UserRepository userRepository;    
    
    @Autowired    
    private PasswordEncoder passwordEncoder;    
    
    @Test    
    public void testCreateUser() {                
        String login = "testLogins";        
        String password = "password";        
        String roleName = "ROLE_TEST";        
        
        RoleEntity roleTest = new RoleEntity();        
        roleTest.setName(roleName);        
        roleRepository.save(roleTest);        
        UserDTO userDTO = userService.createUser(new UserDTO(login, password), Optional.of(roleTest));
        assertNotNull(userDTO.getId());        
        assertEquals(login, userDTO.getLogin());        
        UserEntity savedEntity = userRepository.findByLogin(login).orElse(null);        
        assertNotNull(savedEntity, "User not found in the database!");       
        boolean matches = passwordEncoder.matches(password, savedEntity.getPassword());        
        assertTrue(matches);        
        assertNotNull(savedEntity.getCreatedAt());        
        assertEquals(roleName, savedEntity.getRoleEntity().getName());            
        
    }    
    
    @Test    
    public void testCreateUser_ExistingUser() {  
        String login = "existingUser";              
        String roleName = "ROLE_TEST"; 
        
        RoleEntity roleTest = new RoleEntity();        
        roleTest.setName(roleName);        
        roleRepository.save(roleTest);

        UserEntity existingUser = new UserEntity();        
        existingUser.setLogin(login);        
        existingUser.setPassword(passwordEncoder.encode("oldpass"));        
        existingUser.setRoleEntity(roleTest);        
        userRepository.save(existingUser);        
    
        UserDTO userDTO = new UserDTO();        
        userDTO.setLogin(login);        
        userDTO.setPassword("newpass");        
        UserDTO result = userService.createUser(userDTO, Optional.of(roleTest));        
       
        assertEquals(existingUser.getId(), result.getId());              
        UserEntity updatedUser = userRepository.findByLogin(login).orElse(null);        
        assertTrue(passwordEncoder.matches("oldpass", updatedUser.getPassword()));    
    }
    
}