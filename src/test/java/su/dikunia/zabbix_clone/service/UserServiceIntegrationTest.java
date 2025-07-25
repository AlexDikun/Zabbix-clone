package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.dto.UserDTO;
import su.dikunia.zabbix_clone.repos.RoleRepository;
import su.dikunia.zabbix_clone.repos.UserRepository;


@SpringBootTest
@ContextConfiguration(classes = TestPropertyConfig.class)
@ActiveProfiles("test")
@ComponentScan(basePackages = "su.dikunia.zabbix_clone")
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceIntegrationTest.class);

    @Test
    @Transactional
    public void testCreateUser() {
        
        try {
            String login = "testLogin";
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

        } catch (Exception e) {
            logger.error("CRUSH", e);
        }
    }
}