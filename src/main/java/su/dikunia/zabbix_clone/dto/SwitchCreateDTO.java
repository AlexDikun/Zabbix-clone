package su.dikunia.zabbix_clone.dto;

import org.locationtech.jts.geom.Point;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import su.dikunia.zabbix_clone.domain.SwitchEntity;

@Data
public class SwitchCreateDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String model;
    
    @NotBlank
    private String ipAddress;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude; 

    public SwitchCreateDTO() {};

    public SwitchCreateDTO(
        String name,
        String model,
        String ipAddress,
        Double latitude,
        Double longitude
    ) {
        this.name = name;
        this.model = model;
        this.ipAddress = ipAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    };

    public static SwitchCreateDTO fromEntity(SwitchEntity switchEntity) {
        SwitchCreateDTO dto = new SwitchCreateDTO();
        
        dto.setId(switchEntity.getId());
        dto.setName(switchEntity.getName());
        dto.setModel(switchEntity.getModel());
        dto.setIpAddress(switchEntity.getIpAddress());

        Point coordinates = switchEntity.getCoordinates();
        if (coordinates != null) {
            dto.setLatitude(coordinates.getY()); 
            dto.setLongitude(coordinates.getX()); 
        }
        
        return dto;
    }
    
}
