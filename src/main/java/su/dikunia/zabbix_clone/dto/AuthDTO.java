package su.dikunia.zabbix_clone.dto;

import lombok.Data;

@Data
public class AuthDTO {

    private String login;

    private String password;

    public AuthDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
}