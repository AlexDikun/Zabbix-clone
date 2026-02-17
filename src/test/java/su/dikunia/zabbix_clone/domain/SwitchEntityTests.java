package su.dikunia.zabbix_clone.domain;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static su.dikunia.zabbix_clone.testutils.GeometryTestUtils.point;
import static org.assertj.core.api.Assertions.assertThat;

public class SwitchEntityTests {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSwitchEntityValidation() {
        System.out.println("Создаем валидный коммутатор!");

        SwitchEntity switchEntity = new SwitchEntity();
        switchEntity.setName("Elm Street");
        switchEntity.setModel("ORION-228");
        switchEntity.setIpAddress("203.0.113.142"); 
        switchEntity.setCoordinates(point(150.570892, -33.697978)); // Springwood, Elm Street

        Set<ConstraintViolation<SwitchEntity>> violations = validator.validate(switchEntity);
        assertThat(violations).isEmpty();
    }

    @Test
    void testSwitchEntityValidationWithNullCoordinates() {
        System.out.println("Попытка создать коммутатор без заданных координат!");

        SwitchEntity switchEntity = new SwitchEntity();
        switchEntity.setName("Elm Street");
        switchEntity.setModel("ORION-228");
        switchEntity.setIpAddress("203.0.113.142"); 
        switchEntity.setCoordinates(null);
        Set<ConstraintViolation<SwitchEntity>> violations = validator.validate(switchEntity);

        assertThat(violations).isNotEmpty(); 
        assertThat(violations)
            .extracting(ConstraintViolation::getMessage)
            .contains("Coordinates must be specified"); 
    }
    
}
