package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.config.SecurityConfiguration;
import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.domain.UserEntity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {TestPropertyConfig.class, SecurityConfiguration.class})
public class UserServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userServiceWithMocks;

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
        entityManager.persist(roleEntity);

        UserEntity userEntity = userServiceWithMocks.createUser(login, password, roleEntity);

        assertNotNull(userEntity.getId());
        assertEquals(login, userEntity.getLogin());
        assertNotNull(userEntity.getCreatedAt());

        assertEquals(roleName, userEntity.getRoleEntity().getName());

        verify(passwordEncoder, times(1)).encode(password);
    }
}