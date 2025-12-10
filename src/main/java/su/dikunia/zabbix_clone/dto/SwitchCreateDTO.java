package su.dikunia.zabbix_clone.dto;

import lombok.Data;
import su.dikunia.zabbix_clone.domain.SwitchEntity;

@Data
public class SwitchCreateDTO {

    private Long id;

    private String name;

    private String model;
    
    private String ipAddress;

    private String coordinates;

    public static SwitchCreateDTO fromEntity(SwitchEntity switchEntity) {
        SwitchCreateDTO dto = new SwitchCreateDTO();
        
        dto.setId(switchEntity.getId());
        dto.setName(switchEntity.getName());
        dto.setModel(switchEntity.getModel());
        dto.setIpAddress(switchEntity.getIpAddress());
        dto.setCoordinates(switchEntity.getCoordinates());
        
        return dto;
    }
    
}
