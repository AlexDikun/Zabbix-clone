package su.dikunia.zabbix_clone.domain;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


@TestPropertySource(properties = {
    "spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}"
})
public class RoleEntityTests {

    private static Validator validator; 

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testRoleValidation() {
        System.out.println("Создаем валидную роль!");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("ROLE_USER");
        Set<ConstraintViolation<RoleEntity>> violations = validator.validate(roleEntity);
        assertThat(violations).isEmpty();
    }

    @Test
    void testRoleValidationWithBlankName() {
        System.out.println("Попытка создать роль с пустым логином!");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(""); 
        Set<ConstraintViolation<RoleEntity>> violations = validator.validate(roleEntity);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream().anyMatch(violation -> violation.getMessage().contains("name must not be blank"))).isTrue();
    }
}
