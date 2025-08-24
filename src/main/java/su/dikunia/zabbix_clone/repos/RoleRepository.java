package su.dikunia.zabbix_clone.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import su.dikunia.zabbix_clone.domain.RoleEntity;
import su.dikunia.zabbix_clone.enums.RoleName;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleName name);
}
