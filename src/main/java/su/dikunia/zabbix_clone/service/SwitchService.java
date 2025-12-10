package su.dikunia.zabbix_clone.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import su.dikunia.zabbix_clone.domain.SwitchEntity;
import su.dikunia.zabbix_clone.dto.SwitchCreateDTO;
import su.dikunia.zabbix_clone.enums.SwitchState;
import su.dikunia.zabbix_clone.repos.SwitchRepository;

@Service
public class SwitchService {
    
    @Autowired
    private SwitchRepository switchRepository;
    
    private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Transactional
    public SwitchCreateDTO createSwitch(SwitchCreateDTO switchCreateDTO) {
        Point coordinates = geometryFactory.createPoint(
            new Coordinate(switchCreateDTO.getLongitude(), switchCreateDTO.getLatitude())
        );

        SwitchEntity switchEntity = new SwitchEntity(null, null, null);
        switchEntity.setName(switchCreateDTO.getName());
        switchEntity.setModel(switchCreateDTO.getModel());
        switchEntity.setIpAddress(switchCreateDTO.getIpAddress());
        switchEntity.setCoordinates(coordinates);
        switchEntity.setState(SwitchState.INACTIVE);
   
        switchEntity = switchRepository.save(switchEntity);

        return SwitchCreateDTO.fromEntity(switchEntity);
    }
    
}
