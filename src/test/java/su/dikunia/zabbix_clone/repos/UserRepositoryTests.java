package su.dikunia.zabbix_clone.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import su.dikunia.zabbix_clone.domain.UserEntity;

public class UserRepositoryTests {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByLoginSuccessfully() {
        System.out.println("Сценарий успешного поиска пользователя по логину в репозитории");

        String login = "test@company.ru";
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(userEntity));
        Optional<UserEntity> foundUser = userRepository.findByLogin(login);

        assertTrue(foundUser.isPresent());
        assertEquals(login, foundUser.get().getLogin());
    }

    @Test
    void testFindByLoginNotFound() {
        System.out.println("Сценарий неуспешного поиска пользователя в репозитории");

        String login = "Login_NON_EXISTENT";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        Optional<UserEntity> foundUser = userRepository.findByLogin(login);

        assertTrue(foundUser.isEmpty());
    }
    
    
}
