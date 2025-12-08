package su.dikunia.zabbix_clone.dto;

import lombok.Data;

@Data
public class SwitchCreateDTO {

    private String name;

    private String model;
    
    private String ipAddress;

    private String coordinates;
    
}
