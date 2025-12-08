package su.dikunia.zabbix_clone.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import su.dikunia.zabbix_clone.domain.SwitchEntity;

public interface SwitchRepository extends JpaRepository<SwitchEntity, Long>  {}
