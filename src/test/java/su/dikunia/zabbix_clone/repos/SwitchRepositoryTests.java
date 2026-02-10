package su.dikunia.zabbix_clone.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import static su.dikunia.zabbix_clone.testutils.GeometryTestUtils.point;
import su.dikunia.zabbix_clone.domain.SwitchEntity;

public class SwitchRepositoryTests {

    @Mock
    private SwitchRepository switchRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenExistsByName_thenReturnTrue() {
        System.out.println("Сценарий успешной проверки наличия сущности в репозитории во параметру name");

        SwitchEntity switchEntity = new SwitchEntity();
        switchEntity.setName("Elm Street");
        switchEntity.setModel("ORION-228");
        switchEntity.setIpAddress("203.0.113.142"); 
        switchEntity.setCoordinates(point(150.570892, -33.697978));

        when(switchRepository.existsByName("Elm Street")).thenReturn(true);

        boolean exists = switchRepository.existsByName("Elm Street");

        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsByName_thenReturnFalse() {
        System.out.println("Сценарий не успешной проверки наличия сущности в репозитории во параметру name");

        when(switchRepository.existsByName("Elm Street")).thenReturn(true);
        boolean exists = switchRepository.existsByName("NonExistentSwitch");

        assertThat(exists).isFalse();
    }
    
}
