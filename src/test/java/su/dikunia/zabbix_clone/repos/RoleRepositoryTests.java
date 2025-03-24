package su.dikunia.zabbix_clone.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import su.dikunia.zabbix_clone.domain.RoleEntity;

public class RoleRepositoryTests {

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByRoleNameSuccessfully() {
        System.out.println("Сценарий успешного поиска в репозитории");

        String roleName = "ROLE_INTERN";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(roleEntity));
        Optional<RoleEntity> foundRole = roleRepository.findByName(roleName);

        assertTrue(foundRole.isPresent());
        assertEquals(roleName, foundRole.get().getName());
    }

    @Test
    void testFindByNameNotFound() {
        System.out.println("Сценарий неуспешного поиска в репозитории");

        String roleName = "ROLE_NON_EXISTENT";

        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());
        Optional<RoleEntity> foundRole = roleRepository.findByName(roleName);

        assertTrue(foundRole.isEmpty());
    }
    
}
