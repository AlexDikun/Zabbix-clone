package su.dikunia.zabbix_clone.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static su.dikunia.zabbix_clone.testutils.GeometryTestUtils.point;

import org.locationtech.jts.geom.Point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import su.dikunia.zabbix_clone.domain.SwitchEntity;
import su.dikunia.zabbix_clone.dto.SwitchCreateDTO;
import su.dikunia.zabbix_clone.exceptions.SwitchAlreadyExistsException;
import su.dikunia.zabbix_clone.repos.SwitchRepository;

public class SwitchServiceTest {

    @Mock
    private SwitchRepository switchRepository;

    @InjectMocks
    private SwitchService switchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSwitch_success() {
        System.out.println("Сценарий успешного создания коммутатора!");

        Long id = 56L;
        String name = "Elm Street";
        String model = "ORION-228";
        String ipAddress = "203.0.113.142";
        Point coordinates = point(150.570892, -33.697978);

        SwitchEntity newSwitchEntity = new SwitchEntity();
        newSwitchEntity.setId(id);
        newSwitchEntity.setName(name);
        newSwitchEntity.setModel(model);
        newSwitchEntity.setIpAddress(ipAddress); 
        newSwitchEntity.setCoordinates(coordinates);

        SwitchCreateDTO dto = SwitchCreateDTO.fromEntity(newSwitchEntity);

        when(switchRepository.existsByName(name)).thenReturn(false);
        when(switchRepository.save(any(SwitchEntity.class))).thenReturn(newSwitchEntity);
       
        SwitchCreateDTO switchDTO = switchService.createSwitch(dto);

        assertNotNull(switchDTO);
        assertNotNull(switchDTO.getId());
        assertEquals(name, switchDTO.getName());

        verify(switchRepository, times(1)).existsByName(name);
        verify(switchRepository, times(1)).save(any(SwitchEntity.class));
    }

    @Test
    public void testCreateSwitch_switchAlreadyExists() {
        System.out.println("Сценарий: коммутатор уже существует!");

        String name = "existingSwitch";
        Long id = 62L;
        String model = "ORION-228";
        String ipAddress = "203.0.113.142";
        Point coordinates = point(150.570892, -33.697978);

        SwitchEntity existingSwitch = new SwitchEntity();
        existingSwitch.setName(name);
        existingSwitch.setCoordinates(coordinates);
        existingSwitch.setId(id);
        existingSwitch.setModel(model);
        existingSwitch.setIpAddress(ipAddress);

        SwitchCreateDTO dto = new SwitchCreateDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setModel(model);
        dto.setIpAddress(ipAddress);
        dto.setLongitude(150.570892);
        dto.setLatitude(-33.697978);

        when(switchRepository.existsByName(name)).thenReturn(true);

        assertThrows(
            SwitchAlreadyExistsException.class,
            () -> switchService.createSwitch(dto),
             "Ожидалось исключение SwitchAlreadyExistsException"
        );;

        verify(switchRepository, times(1)).existsByName(name);
    }

}
