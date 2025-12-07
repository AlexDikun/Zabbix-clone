package su.dikunia.zabbix_clone.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.repos.UserRepository;

@Service
public class DeletionScheduler {
    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 3 1 * ?")
    @Transactional
    public void deleteArchivedUsers() {
        LocalDateTime timeLimit = LocalDateTime.now();
        List<UserEntity> usersToDelete = userRepository.findDeletedBefore(timeLimit);
        userRepository.deleteAll(usersToDelete);
    }
}