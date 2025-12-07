package su.dikunia.zabbix_clone.repos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import su.dikunia.zabbix_clone.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);

    @Query("SELECT u FROM UserEntity u WHERE u.deleted = false")
    List<UserEntity> findAllActive();

    @Query("SELECT u FROM UserEntity u")
    List<UserEntity> findAllIncludingDeleted();

    @Query("SELECT u FROM UserEntity u WHERE u.deleted = true AND u.deletedAt <= :thresholdDate")
    List<UserEntity> findDeletedBefore(@Param("thresholdDate") LocalDateTime thresholdDate);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.roleEntity.name = 'ADMIN' AND u.deleted = false")
    long countActiveAdmins();
}
