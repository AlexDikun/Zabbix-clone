package su.dikunia.zabbix_clone.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import su.dikunia.zabbix_clone.config.SetupDataLoader;
import su.dikunia.zabbix_clone.domain.UserEntity;
import su.dikunia.zabbix_clone.repos.UserRepository;

@SpringBootTest
@MockBean(SetupDataLoader.class) 
class DeletionSchedulerTest {
    static {        
        System.setProperty("jwt.secret", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.e30.QwoOdMKxwpjCuC14w47CisOQw74wD8OKw6p7cmjDpsKnHcOfw4lKHcKLZyNjOQ");    
    }    

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private DeletionScheduler deletionScheduler;

    @Test
    void deleteArchivedUsers_shouldDeleteUsersWithExpiredRetention() {
        UserEntity expiredUser = new UserEntity();
        expiredUser.setDeletedAt(LocalDateTime.now().minusDays(1)); 

        UserEntity unexpiredUser = new UserEntity();
        unexpiredUser.setDeletedAt(LocalDateTime.now().plusDays(1)); 

        when(userRepository.findDeletedBefore(any(LocalDateTime.class)))
            .thenReturn(Collections.singletonList(expiredUser));

        deletionScheduler.deleteArchivedUsers();

        verify(userRepository, times(1)).deleteAll(Arrays.asList(expiredUser));
    }
}
