package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.config.SecurityConfiguration;
import su.dikunia.zabbix_clone.config.TestBeansConfig;
import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.config.TestSecurityConfiguration;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.repos.RoleRepository;
import su.dikunia.zabbix_clone.repos.UserRepository;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@Import(SecurityConfiguration.class)
@ContextConfiguration(classes = {TestPropertyConfig.class, TestSecurityConfiguration.class}) 
@ActiveProfiles("test")
@ComponentScan(basePackages = {"su.dikunia.zabbix_clone.repos", "su.dikunia.zabbix_clone.services"})
public class UserServiceIntegrationTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void testCreateUser() {
        
        String login = "testLogins";
        String password = "password";
        String roleName = "ROLE_TEST";

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);
        roleRepository.save(roleEntity);

        UserDTO userDTO = userService.createUser(new UserDTO(login, password), Optional.of(roleEntity));

        assertNotNull(userDTO.getId());
        assertEquals(login, userDTO.getLogin());

        UserEntity userEntity = userRepository.findByLogin(login).orElse(null);
        assertNotNull(userEntity);

        boolean matches = passwordEncoder.matches(password, userEntity.getPassword());
        assertTrue(matches);

        assertNotNull(userEntity.getCreatedAt());
        assertEquals(roleName, userEntity.getRoleEntity().getName()); 
       
    }
}