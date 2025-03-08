package su.dikunia.zabbix_clone.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import su.dikunia.zabbix_clone.domain.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRoleName(String name);
}
