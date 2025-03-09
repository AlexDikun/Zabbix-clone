package su.dikunia.zabbix_clone.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import su.dikunia.zabbix_clone.config.SecurityConfiguration;
import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.repos.RoleRepository;
import su.dikunia.zabbix_clone.repos.UserRepository;

@SpringBootTest
@ContextConfiguration(classes = {TestPropertyConfig.class, SecurityConfiguration.class})
public class UserEntityTests {

    private static Validator validator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("ROLE_TEST");

        when(roleRepository.findByName("ROLE_TEST")).thenReturn(Optional.of(roleEntity));
    }

    private UserEntity createUserEntity(String login, String password, boolean encodePassword) {
        UserEntity userEntity = new UserEntity();
        userEntity.setLogin(login);
        userEntity.setPassword(encodePassword ? passwordEncoder.encode(password) : password);
        userEntity.setCreatedAt(LocalDateTime.now());

        RoleEntity roleEntity = roleRepository.findByName("ROLE_TEST")
                                               .orElseThrow(() -> new RuntimeException("Role not found"));
        userEntity.setRoleEntity(roleEntity);

        return userEntity;
    }

    @Test
    void testUserEntityValidation() {
        System.out.println("Создаем валидного пользователя!");

        UserEntity userEntity = createUserEntity("test@company.ru", "secret", false);

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);
        assertThat(violations).isEmpty();
    }

    @Test
    void testUserEntityValidationWithBlankLogin() {
        System.out.println("Попытка создать пользователя с пустым логином!");

        UserEntity userEntity = createUserEntity("", "secret", false);

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(violation -> violation.getMessage().contains("login must not be blank"))).isTrue();
    }

    @Test
    void testUserEntityValidationWithNullLogin() {
        System.out.println("Попытка создать пользователя с Null логином!");

        UserEntity userEntity = createUserEntity(null, "secret", false);

        doThrow(new DataIntegrityViolationException("NotNull constraint violated"))
            .when(userRepository)
            .save(userEntity);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(userEntity);
        });
    }

    @Test
    void testUserEntityValidationWithBlankPassword() {
        System.out.println("Попытка создать пользователя с пустым паролем!");

        UserEntity userEntity = createUserEntity("test@company.ru", "", false);

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(violation -> violation.getMessage().contains("password must not be blank"))).isTrue();
    }

    @Test
    void testUserEntityValidationWithShortPassword() {
        System.out.println("Попытка создать пользователя с коротким паролем!");

        UserEntity userEntity = createUserEntity("test@company.ru", "1", false);

        Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(violation -> violation.getMessage().contains("size error"))).isTrue();
    }

    @Test
    void testCreationTimestampWithMock() {
        System.out.println("Сценарий проверки заполнения даты и времени сущности в момент ее создания!");

        UserEntity userEntity = createUserEntity("test@company.ru", "secret", true);

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        userRepository.save(userEntity);

        assertNotNull(userEntity.getCreatedAt());
        assertTrue(userEntity.getCreatedAt().isBefore(LocalDateTime.now()));
        verify(userRepository, times(1)).save(userEntity);
    }
}
