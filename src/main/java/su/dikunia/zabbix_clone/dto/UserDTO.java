package su.dikunia.zabbix_clone.dto;

import lombok.Data;

@Data
public class UserDTO {
    
    private Long id;

    private String login;

    private String password;

    public UserDTO() {}
    
    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
}
