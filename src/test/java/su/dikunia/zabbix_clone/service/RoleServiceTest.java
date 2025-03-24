package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.repos.RoleRepository;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRole_NewRole() {
        String roleName = "ROLE_TEST";
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleName);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);

        RoleEntity result = roleService.createRole(roleName);

        assertNotNull(result);
        assertEquals(roleName, result.getName());
        verify(roleRepository, times(1)).save(any(RoleEntity.class));
    }

    @Test
    public void testCreateRole_ExistingRole() {
        String roleName = "ROLE_USER";
        RoleEntity existingRole = new RoleEntity();
        existingRole.setName(roleName);

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(existingRole));

        RoleEntity result = roleService.createRole(roleName);

        assertNotNull(result);
        assertEquals(roleName, result.getName());
        verify(roleRepository, never()).save(any(RoleEntity.class));
    }
}

