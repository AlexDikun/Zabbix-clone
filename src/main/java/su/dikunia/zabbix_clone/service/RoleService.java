package su.dikunia.zabbix_clone.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.enums.RoleName;
import su.dikunia.zabbix_clone.repos.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public RoleEntity createRole(final RoleName name) {
        return roleRepository.findByName(name)
            .orElseGet(() -> {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setName(name);
                return roleRepository.save(roleEntity);
            });
    }
}
