package su.dikunia.zabbix_clone.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import su.dikunia.zabbix_clone.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
}