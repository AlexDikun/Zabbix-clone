package su.dikunia.zabbix_clone.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import su.dikunia.zabbix_clone.config.TestPropertyConfig;
import su.dikunia.zabbix_clone.enums.RoleName;


@SpringBootTest
@ContextConfiguration(classes = TestPropertyConfig.class)
public class RoleEntityTests {
    private static Validator validator;
    private RoleEntity roleEntity;
    @Mock
    private EntityManager entityManager;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void init() {
        roleEntity = new RoleEntity();
        roleEntity.setName(RoleName.ADMIN); // Используем RoleName вместо строки
    }

    @Test
    void testRoleEntityValidation() {
        System.out.println("Создаем валидную роль!");
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleName.MODER); // Используем RoleName
        Set<ConstraintViolation<RoleEntity>> violations = validator.validate(roleEntity);
        assertThat(violations).isEmpty();
    }

    @Test
    void testRoleEntityValidationWithNullName() {
        System.out.println("Попытка создать роль с пустым именем!");
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(null);
        Set<ConstraintViolation<RoleEntity>> violations = validator.validate(roleEntity);

        assertThat(violations).isNotEmpty();
        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Role name must not be null"); 
    }

    @Test
    public void testCreationTimestampWithMock() {
        System.out.println("Сценарий проверки заполнения даты и времени сущности в момент ее создания!");
        LocalDateTime now = LocalDateTime.now();
        roleEntity.setCreatedAt(now);
        entityManager.persist(roleEntity);
        assertNotNull(roleEntity.getCreatedAt());
        LocalDateTime createdAt = roleEntity.getCreatedAt();
        assertTrue(createdAt.isBefore(LocalDateTime.now()));
        verify(entityManager, times(1)).persist(roleEntity);
    }
}
