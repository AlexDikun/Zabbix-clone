package su.dikunia.zabbix_clone.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import su.dikunia.zabbix_clone.domain.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
