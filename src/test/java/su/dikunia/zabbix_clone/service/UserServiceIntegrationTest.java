package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.repos.RoleRepository;


@DataJpaTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "su.dikunia.zabbix_clone")
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testCreateUser() {
        String login = "testLogin";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String roleName = "ROLE_TEST";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);
        roleRepository.save(roleEntity);

        UserEntity userEntity = userService.createUser(login, password, roleEntity);

        assertNotNull(userEntity.getId());
        assertEquals(login, userEntity.getLogin());
        //assertEquals(encodedPassword, userEntity.getPassword());
        //assertNotNull(userEntity.getCreatedAt());
        assertEquals(roleName, userEntity.getRoleEntity().getName());

        //verify(passwordEncoder, times(1)).encode(password);
    }
}